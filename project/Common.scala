import sbt.Keys._
import sbt._

object Common {
  val settings: Seq[Setting[_]] = Seq(
    organization := "ch.yulu",
    version := "1.0.0-SNAPSHOT",
    scalaVersion := "2.11.2"
  )
}
