package com.nbo.controllers

import javax.inject.{Inject, Singleton}
import play.api.libs.json._
import play.api.mvc._
import scala.collection.mutable
import com.nbo.models.{Question, QuestionOption, QuestionDto}
import play.api.Configuration
import com.nbo.repositories.QuestionRepository
import scala.concurrent.{ExecutionContext, Future}
import play.api.Logging

class QuizController @Inject()(val controllerComponents: ControllerComponents, questionRepository: QuestionRepository)(implicit val ec: ExecutionContext)
  extends BaseController with Logging {

  // Mutable list to hold questions
  private val questionsList = new mutable.ListBuffer[Question]()

  // JSON formatters
  implicit val questionOptionFormat: OFormat[QuestionOption] = Json.format[QuestionOption]
  implicit val questionFormat: OFormat[Question] = Json.format[Question]

  
  def getAll(): Action[AnyContent] = Action.async {
    questionRepository.getAllQuestions().map { questions =>
        Ok(Json.toJson(questions))
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

  
  def create(): Action[JsValue] = Action.async(parse.json) { request => {
    
      request.body.validate[QuestionDto] match {
      
        case JsSuccess(newQuestion, _) => 
       
          val question = QuestionDto(newQuestion.content, newQuestion.options)
          

          questionRepository.insertQuestion(question).map { question =>
            Created(Json.toJson(question))
          }
        case JsError(errors) =>
          Future.successful(BadRequest(Json.obj("error" -> "Invalid JSON", "details" -> errors.mkString(", "))))
      }
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
