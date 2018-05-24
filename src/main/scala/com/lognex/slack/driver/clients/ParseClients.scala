package com.lognex.slack.driver.clients

import java.nio.charset.Charset

import com.lognex.slack.driver._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class ParseResponse(messages: Seq[Message], versions: Versions)

object ParseClients {

  def parse(prevVersions: Versions): Future[ParseResponse] = {

    def selectVersion(m: Option[Message], prev: String) = {
      if(m.map(_.version).exists(_ > prev))
        m.get.version
      else prev
    }

    for {
      atolMessage <- parseResource(Atol)
      piritMessage <- parseResource(Pirit)
      shtrihMessage <- parseResource(ShtrihM)
    } yield ParseResponse(
      messages = Seq(atolMessage, piritMessage, shtrihMessage).flatten,
      versions = Versions(
        atol = selectVersion(atolMessage, prevVersions.atol),
        pirit = selectVersion(piritMessage, prevVersions.pirit),
        shtrihM = selectVersion(shtrihMessage, prevVersions.shtrihM)
      )
    )
  }

  private def parseResource(vendor: Vendor): Future[Option[Message]] = {
    import dispatch._

    Http.default(
      url(vendor.resource).GET
        .setContentType("text/html", Charset.defaultCharset())
    ).map { r =>
      Scraper.message(vendor, r.getResponseBodyAsStream)
    }
  }
}
