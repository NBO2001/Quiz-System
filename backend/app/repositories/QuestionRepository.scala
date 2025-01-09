package com.nbo.repositories

import scala.concurrent.{ExecutionContext, Future}
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.{MongoClient, MongoCollection}
import com.nbo.utils.AppConfig
import javax.inject.Inject
import com.nbo.models.{Question, QuestionDto}
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

  /**
   * Inserts a new question into the database.
   *
   * @param question The QuestionDto to be inserted.
   * @return A Future containing the inserted Question with a MongoDB ObjectId.
   */
  def insertQuestion(question: QuestionDto): Future[Question] = {
    val document = Document(Json.toJson(question).toString())

    collection.insertOne(document).toFuture()
      .map(_ => {
        val insertedId = document.getObjectId("_id").toHexString
        Question(
          id = insertedId,
          content = question.content,
          options = question.options
        )
      })
      .recover {
        case ex: Throwable =>
          println(s"Error inserting question: ${ex.getMessage}")
          throw ex
      }
  }

  /**
   * Cleans up the MongoDB client resources.
   */
  def close(): Unit = client.close()

  // Utility method to parse a MongoDB document to a Question object
  private def parseDocumentToQuestion(doc: Document): Option[Question] = {
    try {
      val id = doc.getObjectId("_id").toHexString
      val questionJson = Json.parse(doc.toJson())
      Some(questionJson.as[Question].copy(id = id))
    } catch {
      case ex: Throwable =>
        println(s"Error parsing document to Question: ${ex.getMessage}")
        None
    }
  }
}
