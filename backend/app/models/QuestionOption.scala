package com.nbo.models

import play.api.libs.json.{Json, OFormat}

case class QuestionOption(id: Long, text: String, isCorrect: Boolean)

object QuestionOption {
  implicit val questionOptionFormat: OFormat[QuestionOption] = Json.format[QuestionOption]
}