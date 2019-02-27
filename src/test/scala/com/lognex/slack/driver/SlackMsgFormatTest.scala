package com.lognex.slack.driver

import org.scalatest.FunSuite
import ReadWriters._
import play.api.libs.json.Json

class SlackMsgFormatTest extends FunSuite {

  val severalMessages =
    """{"text":"\nНовые версии драйверов\n<http://yandex.ru|1.0> - Атол (10 версия)\n<http://yandex.ru|1.0> - Атол\n<http://yandex.ru|1.333> - Пирит\n<http://yandex.ru|2> - Штрих-М","username":"Driver-tracker bot","icon_emoji":":twilight-sparkle:"}"""

  test("several messages") {
    val messages = Seq(
      Message(Atol10, "1.0", "http://yandex.ru"),
      Message(Atol, "1.0", "http://yandex.ru"),
      Message(Pirit, "1.333", "http://yandex.ru"),
      Message(ShtrihM, "2", "http://yandex.ru")
    )

    assert(Json.stringify(Json.toJson(messages)) == severalMessages)
  }

}
