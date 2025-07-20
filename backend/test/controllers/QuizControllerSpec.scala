package controllers

import org.scalatestplus.play._
import play.api.test.Helpers._
import play.api.test._
import play.api.libs.json._
import com.nbo.models._
import com.nbo.repositories.QuestionRepository
import com.nbo.utils.AppConfig
import play.api.Configuration
import scala.concurrent.{ExecutionContext, Future}

class FakeAppConfig extends AppConfig(Configuration.from(Map("database.url" -> "mongodb://localhost")))

class FakeQuestionRepository()(implicit ec: ExecutionContext) extends QuestionRepository(new FakeAppConfig(), ec) {
  private val sampleQuestion = Question(
    id = "1",
    content = "What is 1 + 1?",
    options = List(
      QuestionOption("1", Some(false)),
      QuestionOption("2", Some(true))
    )
  )

  override def getAllQuestions(): Future[Seq[Question]] = Future.successful(Seq(sampleQuestion))

  override def getQuestion(question_id: String): Future[Option[Question]] =
    Future.successful(if (question_id == sampleQuestion.id) Some(sampleQuestion) else None)

  override def checkAnswer(question_id: String, answer: String): Future[Boolean] =
    Future.successful(question_id == sampleQuestion.id && answer == "2")

  override def insertQuestion(question: QuestionDto): Future[Question] =
    Future.successful(Question("2", question.content, question.options))

  override def deleteQuestion(question_id: String): Future[Boolean] = Future.successful(true)

  override def close(): Unit = ()
}

class QuizControllerSpec extends PlaySpec with Results {
  implicit val ec: ExecutionContext = scala.concurrent.ExecutionContext.Implicits.global
  private val repo = new FakeQuestionRepository()
  private val controller = new QuizController(stubControllerComponents(), repo)

  "QuizController#getById" should {
    "return sanitized question when answered flag is true" in {
      val result = controller.getById("1", true).apply(FakeRequest(GET, "/questions/1"))
      status(result) mustBe OK
      val question = contentAsJson(result).as[Question]
      question.options.forall(_.isCorrect.isEmpty) mustBe true
    }

    "return NotFound when question does not exist" in {
      val result = controller.getById("99").apply(FakeRequest(GET, "/questions/99"))
      status(result) mustBe NOT_FOUND
    }
  }

  "QuizController#checkAnswer" should {
    "return true for correct answers" in {
      val request = FakeRequest(POST, "/questions/check_answer/1").withJsonBody(Json.toJson(OptionDto("2")))
      val result = controller.checkAnswer("1").apply(request)
      status(result) mustBe OK
      (contentAsJson(result) \ "isCorrect").head.as[Boolean] mustBe true
    }
  }
}
