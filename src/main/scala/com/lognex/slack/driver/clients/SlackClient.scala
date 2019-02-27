package com.lognex.slack.driver.clients

import java.nio.charset.Charset

import com.lognex.slack.driver.{Message, ReadWriters}
import play.api.libs.json.Json

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

final class SlackClient(token: String) {

  def sendMsg(msgs: Seq[Message]): Future[Unit] = {
    import ReadWriters._
    import dispatch._

    Http
      .default(
        url(s"https://hooks.slack.com/services/$token").POST
          .setBody(Json.stringify(Json.toJson(msgs)))
          .setContentType("application/json", Charset.defaultCharset())
      )
      .map(_ => ())
  }
}
