package com.nbo.controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._
import scala.collection.mutable
import com.nbo.models.{Question, QuestionOption}

@Singleton
class QuizController @Inject()(val controllerComponents: ControllerComponents)
  extends BaseController {

  // Mutable list to hold questions
  private val questionsList = new mutable.ListBuffer[Question]()

  // JSON formatters
  implicit val questionOptionFormat: OFormat[QuestionOption] = Json.format[QuestionOption]
  implicit val questionFormat: OFormat[Question] = Json.format[Question]

  // Initial setup
  val optionsForQuestion1 = List(
    QuestionOption(1, "bla bla", false),
    QuestionOption(2, "bla bla true", true),
    QuestionOption(3, "bla bla bla", false)
  )
  questionsList += Question(1, "teste", options = optionsForQuestion1)

  // 1. Get all questions
  def getAll(): Action[AnyContent] = Action {
    if (questionsList.isEmpty) {
      NoContent
    } else {
      Ok(Json.toJson(questionsList))
    }
  }

  // 2. Get a question by ID
  def getById(id: Int): Action[AnyContent] = Action {
    val question = questionsList.find(_.id == id)
    question match {
      case Some(q) => Ok(Json.toJson(q))
      case None    => NotFound(Json.obj("error" -> s"Question with ID $id not found"))
    }
  }

  // 3. Create a new question
  def create(): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[Question] match {
      case JsSuccess(newQuestion, _) =>
        if (questionsList.exists(_.id == newQuestion.id)) {
          Conflict(Json.obj("error" -> "A question with this ID already exists"))
        } else {
          questionsList += newQuestion
          Created(Json.toJson(newQuestion))
        }
      case JsError(errors) =>
        BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> errors.mkString(", ")))
    }
  }

  // 4. Update an existing question
  def update(id: Int): Action[JsValue] = Action(parse.json) { request =>
    request.body.validate[Question] match {
      case JsSuccess(updatedQuestion, _) =>
        val index = questionsList.indexWhere(_.id == id)
        if (index >= 0) {
          questionsList.update(index, updatedQuestion)
          Ok(Json.toJson(updatedQuestion))
        } else {
          NotFound(Json.obj("error" -> s"Question with ID $id not found"))
        }
      case JsError(errors) =>
        BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> errors.mkString(", ")))
    }
  }

  // 5. Delete a question by ID
  def delete(id: Int): Action[AnyContent] = Action {
    val index = questionsList.indexWhere(_.id == id)
    if (index >= 0) {
      questionsList.remove(index)
      NoContent
    } else {
      NotFound(Json.obj("error" -> s"Question with ID $id not found"))
    }
  }
}
