package com.lognex.slack.driver

sealed trait Vendor {
  def resource: String
}
case object Atol extends Vendor {
  def resource: String = "http://www.kassa-online.su/2017/08/01/gde-skachat-drajvera-atol/"
}
case object Atol10 extends Vendor {
  def resource: String = "http://www.kassa-online.su/2017/08/01/gde-skachat-drajvera-atol/"
}
case object ShtrihM extends Vendor {
  def resource: String = "https://www.shtrih-m.ru/support/download/?section_id=all&product_id=all&type_id=156&searchDownloads=%D0%A8%D0%A2%D0%A0%D0%98%D0%A5-%D0%9C%3A+%D0%94%D1%80%D0%B0%D0%B9%D0%B2%D0%B5%D1%80+%D0%9A%D0%9A%D0%A2+"
}
case object Pirit extends Vendor {
  def resource: String = "https://help.dreamkas.ru/hc/ru/articles/115000919269-%D0%91%D0%B8%D0%B1%D0%BB%D0%B8%D0%BE%D1%82%D0%B5%D0%BA%D0%B0-Piritlib-%D0%B4%D0%BB%D1%8F-Pirit-%D0%B8-%D0%92%D0%B8%D0%BA%D0%B8-%D0%9F%D1%80%D0%B8%D0%BD%D1%82-%D0%A4"
}

case class Message(vendor: Vendor, version: String, link: String)
