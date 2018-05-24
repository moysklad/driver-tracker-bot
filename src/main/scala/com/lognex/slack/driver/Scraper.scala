package com.lognex.slack.driver

import java.io.InputStream

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import net.ruippeixotog.scalascraper.dsl.DSL.Extract._
import net.ruippeixotog.scalascraper.dsl.DSL._

object Scraper {

  def message(vendor: Vendor, stream: InputStream): Option[Message] =
    vendor match {
      case Pirit   => genPiritMessage(stream)
      case Atol    => genAtolMessage(stream)
      case ShtrihM => genShtrihmMessage(stream)
    }

  private def genPiritMessage(input: InputStream): Option[Message] = {
    val browser = JsoupBrowser()
    val links = browser.parseInputStream(input) >> elementList("a") >> attr(
      "href")
    links collectFirst {
      case piritVersionRegexp(link, version) =>
        Message(Pirit, version, piritHost + link)
    }
  }

  private def genShtrihmMessage(input: InputStream): Option[Message] = {
    val browser = JsoupBrowser()
    val tds = browser.parseInputStream(input) >> element(".pageCont") >> element(
      "table") >> elementList("td")
    for {
      version <- tds
        .lift(1)
        .map(el => el >> text)
        .collect({ case shtrihVersionRegexp(v) => v })
      link <- tds.lift(4).map(el => el >> element("a") >> attr("href"))
    } yield Message(ShtrihM, version, shtrihHost + link)
  }

  private def genAtolMessage(input: InputStream): Option[Message] = {
    val browser = JsoupBrowser()
    val elements = browser.parseInputStream(input) >> elementList("p") >?> (text, element(
      "a"))

    val message = elements
      .collect({
        case (Some(atolVersionRegexp(version)), Some(link)) =>
          Message(Atol, version, link >> attr("href"))
      })
      .sorted(Ordering.by[Message, String](_.version).reverse)
      .headOption

    message
  }

  private val piritVersionRegexp = "(.*PiritLib-v*(.*)\\.7z)$".r
  private val shtrihVersionRegexp = "(.*)\\s\\(.*$".r
  private val atolVersionRegexp = "(9.*)\\s—.*$".r
  private val shtrihHost = "https://www.shtrih-m.ru"
  private val piritHost = "https://help.dreamkas.ru"
}
