name := """yulu"""

Common.settings

libraryDependencies ++= Seq(
  ws,
  "org.webjars" %% "webjars-play" % "2.3.0",
  "org.webjars" % "angularjs" % "1.2.19",
  "org.webjars" % "bootstrap" % "3.2.0" exclude("org.webjars", "jquery"),
  "org.webjars" % "requirejs" % "2.1.14-1" exclude("org.webjars", "jquery")
)

pipelineStages := Seq(rjs, digest, gzip)

lazy val db = project in file("modules/db")

lazy val web = (project in file("modules/web"))
  .enablePlugins(PlayScala)
  .dependsOn(db).aggregate(db)

lazy val users = (project in file("modules/users"))
  .enablePlugins(PlayScala)
  .dependsOn(web, db).aggregate(web, db)

lazy val yulu = (project in file("."))
  .enablePlugins(PlayScala)
  .dependsOn(web, db, users)
  .aggregate(web, db, users)