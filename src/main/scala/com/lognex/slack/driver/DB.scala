package com.lognex.slack.driver

import java.io._

import play.api.libs.json.{Json, Reads, Writes}
import resource._

import scala.util.Try

final class DB(filename: String) {

  def read(implicit r: Reads[Versions]): Versions = {
    (for {
      input <- Try(new FileInputStream(filename)).toOption
      versions <- managed(input).map({
        stream => Json.parse(stream).asOpt
      }).opt
    } yield versions).flatten.getOrElse(Versions("0", "0", "0", "0"))
  }

  def write(versions: Versions)(implicit w: Writes[Versions]): Unit = {
    managed(new BufferedWriter(new FileWriter(filename))).foreach {
      _.write(Json.stringify(w.writes(versions)))
    }
  }
}
