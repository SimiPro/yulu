import play.api.Play.current
import models.Tables.{Tokens, Users}
import play.api.db.DB
import play.api.{GlobalSettings, Logger}

import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.meta.MTable

/**
 * Created by simipro on 9/21/14.
 */
object Global extends GlobalSettings {


  override def beforeStart(app: play.api.Application): Unit = {

    Logger.info("before App Starts")
  }


  override def onStart(app: play.api.Application): Unit = {
    CreateDatabase()
    Logger.info("Application has started");
  }

  override def onStop(app: play.api.Application) {
    Logger.info("Application has Stopped");
  }


  def CreateDatabase() = {
    val db = Database.forDataSource(DB.getDataSource())
    db.withSession { implicit session =>
      if (MTable.getTables("user").list.isEmpty) {
        (Tokens.ddl ++ Users.ddl).create
      }
    }
  }
}
