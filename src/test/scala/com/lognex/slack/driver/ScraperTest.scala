package com.lognex.slack.driver

import java.io.FileInputStream

import org.scalatest.FunSuite

class ScraperTest extends FunSuite {
  import resource._

  val resourceDir = "src/test/resources"

  test("Pirit scraping") {
    val message = managed(new FileInputStream(s"$resourceDir/pirit.html"))
      .map(s => Scraper.message(Pirit, s))
      .opt
      .flatten

    val expectedMessage = Message(
      vendor = Pirit,
      version = "2.0.0.131",
      link = "https://help.dreamkas.ru/hc/ru/article_attachments/360000990165/PiritLib-v2.0.0.131.7z"
    )
    assert(message.contains(expectedMessage))
  }

  test("Shtrih scraping") {
    val message = managed(new FileInputStream(s"$resourceDir/shtrih.html"))
      .map(s => Scraper.message(ShtrihM, s))
      .opt
      .flatten

    val expectedMessage = Message(
      vendor = ShtrihM,
      version = "4.14.688",
      link = "https://www.shtrih-m.ru/docs/download.php?file=27463&iblock=21"
    )
    assert(message.contains(expectedMessage))
  }

  test("Atol scraping") {
    val message = managed(new FileInputStream(s"$resourceDir/atol.html"))
      .map(s => Scraper.message(Atol, s))
      .opt
      .flatten

    val expectedMessage = Message(
      vendor = Atol,
      version = "9.12.1",
      link = "http://fs.atol.ru/_layouts/15/atol.templates/Handlers/FileHandler.ashx?guid=eaa08766-1b89-486f-9b20-c9ab7748c99f&webUrl="
    )
    assert(message.contains(expectedMessage))
  }

  test("Atol 10 scraping") {
    val message = managed(new FileInputStream(s"$resourceDir/atol10.html"))
      .map(s => Scraper.message(Atol10, s))
      .opt
      .flatten

    val expectedMessage = Message(
      vendor = Atol10,
      version = "10.3.1",
      link = "https://dfiles.ru/files/ma1z7tpzi"
    )
    assert(message.contains(expectedMessage))
  }
}
