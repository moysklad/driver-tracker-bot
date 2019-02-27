import AssemblyKeys._

organization := "com.lognex"

name := "driver-tracker-bot"

scalacOptions ++= Seq(
  "-deprecation",
  "-encoding",
  "UTF-8",
  "-feature",
  "-unchecked",
  "-Xlint:-unused,_",
  "-Xfatal-warnings",
  "-language:higherKinds",
  "-Yno-adapted-args",
  "-Ywarn-value-discard"
)

scalaVersion := "2.12.6"

libraryDependencies ++= Seq(
  "com.typesafe.play" %% "play-json" % "2.6.7",
  "net.databinder.dispatch" %% "dispatch-core" % "0.13.3",
  "net.ruippeixotog" %% "scala-scraper" % "2.1.0",
  "com.jsuereth" %% "scala-arm" % "2.0",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.scala-lang.modules" %% "scala-async" % "0.9.7",
  "org.scalatest" %% "scalatest" % "3.0.3" % Test
)

Seq(assemblySettings: _*)
mainClass in assembly := Some("com.lognex.slack.driver.Bot")
jarName in assembly := { s"${name.value}-${version.value}.jar"}
mergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

val repoUrl = "https://repo.moysklad.ru/artifactory/"
publishTo in Global := {
  if (isSnapshot.value) {
    Some("Moysklad Libs Snapshot Repo" at repoUrl + "libs-snapshot-local")
  } else {
    Some("Moysklad Libs Release Repo" at repoUrl + "libs-release-local")
  }
}