name := "users"

Common.settings

libraryDependencies ++= Seq(
  jdbc,
  "ws.securesocial" %% "securesocial" % "2.1.3",
  "com.h2database" % "h2" % "1.4.180",
  "com.typesafe.slick" %% "slick" % "2.1.0",
  "joda-time" % "joda-time" % "2.4",
  "org.joda" % "joda-convert" % "1.6",
  "com.github.tototoshi" %% "slick-joda-mapper" % "1.2.0",
  "org.specs2" % "specs2_2.10" % "2.4.2"
)
