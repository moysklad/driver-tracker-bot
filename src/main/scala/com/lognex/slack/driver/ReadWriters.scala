package com.lognex.slack.driver

import play.api.libs.json._
import play.api.libs.functional.syntax._

object ReadWriters {

  implicit val versionsWriter = (
    (JsPath \ "atol_10").write[String] and
      (JsPath \ "atol").write[String] and
      (JsPath \ "shrihm").write[String] and
      (JsPath \ "pirit").write[String]
  )(unlift(Versions.unapply))

  implicit val versionsReader = (
    (JsPath \ "atol_10").read[String] and
      (JsPath \ "atol").read[String] and
      (JsPath \ "shrihm").read[String] and
      (JsPath \ "pirit").read[String]
    )(Versions.apply _)

  implicit val messageWriter = new Writes[Seq[Message]] {
    override def writes(messages: Seq[Message]): JsValue = {
      val drivers = for {
        msg <- messages
        vendorMsg = msg.vendor match {
          case Atol10 => "Атол (10 версия)"
          case Atol => "Атол"
          case Pirit => "Пирит"
          case ShtrihM => "Штрих-М"
        }
      } yield s"<${msg.link}|${msg.version}> - $vendorMsg"

      Json.obj(
        "text" -> (slackHeader +: drivers).mkString("\n", "\n", ""),
        "username" -> "Driver-tracker bot",
        "icon_emoji" -> ":hankey:"
      )
    }
  }

  private val slackHeader = "Новые версии драйверов"

}
