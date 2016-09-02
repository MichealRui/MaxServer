name := """shopServer"""

version := "1.0"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  filters,
  "com.fasterxml.jackson.dataformat" % "jackson-dataformat-xml" % "2.4.0",
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

