package com.lognex.slack.driver.clients

import java.nio.charset.Charset

import com.lognex.slack.driver._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class ParseResponse(messages: Seq[Message], versions: Versions)

object UpdatesChecker {

  def check(prevVersions: Versions): Future[ParseResponse] = {

    def selectVersion(m: Option[Message], prev: String) = {
      if (m.map(_.version).exists(_ > prev))
        m.get.version
      else prev
    }

    for {
      atol10Message <- checkResource(Atol10)
      atolMessage <- checkResource(Atol)
      piritMessage <- checkResource(Pirit)
      shtrihMessage <- checkResource(ShtrihM)
    } yield
      ParseResponse(
        messages = Seq(atol10Message, atolMessage, piritMessage, shtrihMessage).flatten,
        versions = Versions(
          atol_10 = selectVersion(atol10Message, prevVersions.atol_10),
          atol = selectVersion(atolMessage, prevVersions.atol),
          pirit = selectVersion(piritMessage, prevVersions.pirit),
          shtrihM = selectVersion(shtrihMessage, prevVersions.shtrihM)
        )
      )
  }

  private def checkResource(vendor: Vendor): Future[Option[Message]] = {
    import dispatch._

    Http
      .default(
        url(vendor.resource).GET
          .setContentType("text/html", Charset.defaultCharset())
      )
      .map { r =>
        Scraper.message(vendor, r.getResponseBodyAsStream)
      }
  }
}
