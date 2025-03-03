package com.nbo.controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._
import scala.collection.mutable
import com.nbo.models._
import play.api.Configuration
import com.nbo.repositories.QuestionRepository
import scala.concurrent.{ExecutionContext, Future}
import play.api.Logging

@Singleton
class QuizController @Inject() (
    val controllerComponents: ControllerComponents,
    questionRepository: QuestionRepository
)(implicit val ec: ExecutionContext)
    extends BaseController
    with Logging {

  def getAll(): Action[AnyContent] = Action.async {
    questionRepository.getAllQuestions().map { questions =>
      Ok(Json.toJson(questions))
    }
  }

  def getById(
      question_id: String,
      answered: Boolean = true
  ): Action[AnyContent] = Action.async {
    questionRepository.getQuestion(question_id).map {
      case Some(q) =>
        if (answered) {
          Ok(
            Json
              .toJson(q.copy(options = q.options.map(_.copy(isCorrect = None))))
          )
        } else {
          Ok(Json.toJson(q))
        }
      case None =>
        NotFound(
          Json.obj("error" -> s"Question with ID $question_id not found")
        )
    }
  }

  def checkAnswer(question_id: String): Action[JsValue] =
    Action.async(parse.json) { request =>
      request.body.validate[OptionDto] match {
        case JsSuccess(answer, _) =>
          questionRepository.checkAnswer(question_id, answer.text).map {
            isCorrect =>
              Ok(Json.obj("isCorrect" -> isCorrect))
          }
        case JsError(errors) =>
          Future.successful(
            BadRequest(
              Json.obj(
                "error" -> "Invalid JSON",
                "details" -> errors.mkString(", ")
              )
            )
          )
      }
    }

  def create(): Action[JsValue] = Action.async(parse.json) { request =>
    request.body.validate[QuestionDto] match {
      case JsSuccess(newQuestion, _) =>
        val question = QuestionDto(newQuestion.content, newQuestion.options)
        questionRepository.insertQuestion(question).map { question =>
          Created(Json.toJson(question))
        }
      case JsError(errors) =>
        Future.successful(
          BadRequest(
            Json.obj(
              "error" -> "Invalid JSON",
              "details" -> errors.mkString(", ")
            )
          )
        )
    }
  }

  def delete(question_id: String): Action[AnyContent] = Action.async {
    questionRepository.deleteQuestion(question_id).map { _ =>
      Ok(Json.obj("message" -> "Question deleted"))
    }
  }
}
