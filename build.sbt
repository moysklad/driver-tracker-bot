import AssemblyKeys._

organization := "com.lognex"

name := "driver-tracker-bot"
version := "1.0"

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
  "org.scalatest" %% "scalatest" % "3.0.3" % Test
)

Seq(assemblySettings: _*)
mainClass in assembly := Some("com.lognex.slack.driver.Main")
jarName in assembly := { s"${name.value}-${version.value}.jar"}
mergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}
