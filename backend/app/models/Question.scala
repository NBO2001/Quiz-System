package com.nbo.models

import play.api.libs.json.{Json, OFormat}
import com.nbo.models.QuestionOption

case class Question(id: Int, content: String, options: List[QuestionOption])

// Companion object for JSON formatting
object Question {
  implicit val questionListJson: OFormat[Question] = Json.format[Question]
}
