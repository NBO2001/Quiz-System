package com.nbo.models

import play.api.libs.json.{Json, OFormat}

case class QuestionOption(id: String, text: String, isCorrect: Boolean)

object QuestionOption {
  implicit val questionOptionFormat: OFormat[QuestionOption] = Json.format[QuestionOption]
}

case class QuestionOptionDto( text: String, isCorrect: Boolean)

object QuestionOptionDto {
  implicit val questionOptionDtoFormat: OFormat[QuestionOptionDto] = Json.format[QuestionOptionDto]
}