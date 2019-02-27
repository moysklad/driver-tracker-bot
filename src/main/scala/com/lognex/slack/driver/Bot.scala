package com.lognex.slack.driver

import java.util.concurrent.{Executors, TimeUnit}

import com.lognex.slack.driver.clients.{SlackClient, UpdatesChecker}
import com.typesafe.scalalogging.Logger

import scala.async.Async.{async, await}
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.util.control.NonFatal

object Bot extends App {

  import ReadWriters._

  val logger = Logger[Bot.type]

  def start(token: String) = {
    val slackClient = new SlackClient(token)
    val db = new DB("versions.json")

    val scheduler = Executors.newSingleThreadScheduledExecutor()
    scheduler.scheduleWithFixedDelay(
      () => {
        try {
          Await.result(
            async {
              val prevVersions = db.read
              val response = await(UpdatesChecker.check(prevVersions))
              if (prevVersions != response.versions) {
                logger.info(s"The new version of the drivers has been found - ${response.versions}")
                db.write(response.versions)
                await(slackClient.sendMsg(response.messages))
              } else {
                logger.info("A newer version of the drivers hasn't been found")
              }
            },
            Duration(1, TimeUnit.MINUTES)
          )
        } catch {
          case NonFatal(ex) =>
            logger.error(
              "An error occurred while searching for a new version of drivers",
              ex
            )
            scheduler.shutdown()
        }
      },
      0,
      1,
      TimeUnit.DAYS
    )
  }

  val token = args.headOption.filter(_.nonEmpty)
  if (token.isDefined) {
    start(token.get)
    logger.info("The Driver Tracker Bot is started!")
  } else {
    logger.info("Please, enter the slack token")
  }

}
