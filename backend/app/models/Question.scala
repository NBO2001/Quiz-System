package com.nbo.models

import play.api.libs.json.{Json, OFormat}
import com.nbo.models.QuestionOption
import org.mongodb.scala._
import scala.collection.JavaConverters._

case class Question(id: String, content: String, options: List[QuestionOption])

case class QuestionDto(content: String, options: List[QuestionOption])

object Question {
  implicit val questionListJson: OFormat[Question] = Json.format[Question]
}

object QuestionDto {
  implicit val questionListDtojson: OFormat[QuestionDto] = Json.format[QuestionDto]
}
