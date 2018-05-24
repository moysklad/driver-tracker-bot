package com.lognex.slack.driver

import java.util.concurrent.{Executors, TimeUnit}

import com.lognex.slack.driver.clients.{ParseClients, SlackClient}

import scala.concurrent.ExecutionContext.Implicits.global

object Main extends App {
  import ReadWriters._

  def start(token: String) = {
    val slackClient = new SlackClient(token)
    val db = new DB("versions.json")

    val scheduler = Executors.newSingleThreadScheduledExecutor()
    scheduler.scheduleWithFixedDelay(
      () => {
        try {
          val prevVersions = db.read
          (for {
            response <- ParseClients.parse(prevVersions)
            if prevVersions != response.versions
            _ = db.write(response.versions)
            _ <- slackClient.sendMsg(response.messages)

          } yield ()).foreach(_ => ())
        } catch {
          case ex: Throwable =>
            ex.printStackTrace()
            scheduler.shutdown()
        }
      },
      0,
      1,
      TimeUnit.DAYS
    )
  }

  val token = args(0)
  if(token != null && token.nonEmpty) {
    start(token)
  } else {
    println("Please, enter slack token")
  }



}
