name := "users"

Common.settings

scalaVersion := "2.11.2"

resolvers += "Scalaz Bintray Repo" at "http://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq(
  jdbc,
  "ws.securesocial" % "securesocial_2.11" % "3.0-M1",
  "com.h2database" % "h2" % "1.4.180",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "joda-time" % "joda-time" % "2.4",
  "org.joda" % "joda-convert" % "1.7",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0",
  "org.specs2" % "specs2_2.11" % "2.4.4"
)
