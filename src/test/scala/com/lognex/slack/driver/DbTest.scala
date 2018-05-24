package com.lognex.slack.driver

import org.scalatest.FunSuite

class DbTest extends FunSuite {
  import ReadWriters._

  test("Read version from file") {
    val db = new DB("src/test/resources/versions.json")
    assert(db.read == Versions("1.0", "1.0", "1.0"))
  }

  test("Write version to file") {
    val newVersion = Versions("2.0", "2.0", "2.0")
    val db = new DB("target/versions.json")
    db.write(newVersion)
    assert(db.read == newVersion)
  }

  test("Versions isn't found") {
    val expected = Versions("0", "0", "0")
    val db = new DB("fake")
    val versions = db.read
    assert(versions == expected)
  }

}
