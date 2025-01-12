package com.nbo.repositories

import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.{MongoClient, MongoCollection}
import com.nbo.utils.AppConfig
import javax.inject.Inject
import com.nbo.models.{Question, QuestionDto, QuestionOption}
import play.api.libs.json.Json
import scala.util.{Failure, Success}

/**
 * Repository for managing Question entities in the MongoDB database.
 *
 * @param config Application configuration for database connection.
 * @param ec     Execution context for asynchronous operations.
 */
class QuestionRepository @Inject()(config: AppConfig, implicit val ec: ExecutionContext) {

  // Initialize MongoDB collection
  private lazy val client: MongoClient = MongoClient(config.databaseUrl)
  private lazy val collection: MongoCollection[Document] =
    client.getDatabase("quiz_system").getCollection("questions")

  /**
   * Retrieves all questions from the database.
   *
   * @return A Future sequence of Question objects.
   */
  def getAllQuestions(): Future[Seq[Question]] = {
    collection.find().toFuture()
      .map { documents =>
        documents.flatMap { doc =>
          parseDocumentToQuestion(doc)
        }
      }
      .recover {
        case ex: Throwable =>
          println(s"Error fetching questions: ${ex.getMessage}")
          Seq.empty[Question]
      }
  }

  def getQuestion(question_id: String): Future[Option[Question]] = {
    collection.find(equal("_id", new ObjectId(question_id))).first().toFuture()
      .map { doc =>
        parseDocumentToQuestion(doc)
      }
      .recover {
        case ex: Throwable =>
          println(s"Error fetching question: ${ex.getMessage}")
          None
      }
  }


  def checkAnswer(question_id: String, answer: String): Future[Boolean] = {
    collection.find(equal("_id", new ObjectId(question_id))).first().toFuture()
      .map { doc =>
        val questionJson = Json.parse(doc.toJson())
        val correctAnswer = (questionJson \ "options").as[List[QuestionOption]].find(_.isCorrect.getOrElse(false)).map(_.text)
        correctAnswer.contains(answer)
      }
      .recover {
        case ex: Throwable =>
          println(s"Error fetching question: ${ex.getMessage}")
          false
      }
  }


  /**
  * Inserts a new question into the database and retrieves it with its generated MongoDB ObjectId.
  *
  * @param question The `QuestionDto` object containing the question details to be inserted.
  * @return A `Future` containing the inserted `Question` with its MongoDB ObjectId.
  * @throws Exception If the operation fails at any stage.
  */
  def insertQuestion(question: QuestionDto): Future[Question] = {
    val document = Document(Json.toJson(question).toString())

    collection.insertOne(document).toFuture()
      .flatMap { _ =>
        collection.find(document).first().toFuture()
      }
      .map { insertedDoc =>
        val insertedId = insertedDoc.getObjectId("_id").toHexString 
        Question(
          id = insertedId,
          content = question.content,
          options = question.options
        )
      }
      .recoverWith { case ex: Throwable =>
      
        val errorMessage = s"Failed to insert question: ${ex.getMessage}"
        println(errorMessage) 
        Future.failed(new Exception(errorMessage, ex))
      }
  }


  def deleteQuestion(question_id: String): Future[Boolean] = {
    val objectId = new ObjectId(question_id)
    collection.deleteOne(equal("_id", objectId)).toFuture()
      .map { result =>
        result.wasAcknowledged()
      }
      .recover {
        case ex: Throwable =>
          println(s"Error deleting question: ${ex.getMessage}")
          false
      }
  }

  /**
   * Cleans up the MongoDB client resources.
   */
  def close(): Unit = client.close()

  // Utility method to parse a MongoDB document to a Question object
  private def parseDocumentToQuestion(doc: Document): Option[Question] = {
    try {
      
      val id = doc.getObjectId("_id")
      val questionJson = Json.parse(doc.toJson())
      val question = Question(id = id.toString(), content = (questionJson \ "content").as[String], options = (questionJson \ "options").as[List[QuestionOption]])
      Some(question)
    } catch {
      case ex: Throwable =>
        println(s"Error parsing document to Question: ${ex.getMessage}")
        None
    }
  }
}
