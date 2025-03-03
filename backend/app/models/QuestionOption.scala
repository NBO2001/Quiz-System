package com.nbo.models

import play.api.libs.json.{Json, OFormat}

case class QuestionOption(text: String, isCorrect: Option[Boolean])

object QuestionOption {
  implicit val questionOptionFormat: OFormat[QuestionOption] =
    Json.format[QuestionOption]
}

case class OptionDto(text: String)

object OptionDto {
  implicit val optionDtoFormat: OFormat[OptionDto] = Json.format[OptionDto]
}
