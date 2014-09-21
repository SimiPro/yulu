package core


import models.Tables

import scala.slick.driver.H2Driver.simple._
import org.specs2.execute.{AsResult, Result}
import play.api.db.DB
import play.api.test.Helpers._
import play.api.test.{FakeApplication, WithApplication}

/**
 * Created Simipro
 */
abstract class WithDbData extends WithApplication(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
  implicit lazy val db = Database.forDataSource(DB.getDataSource())

  override def around[T: AsResult](t: => T): Result = super.around {
    prepareDbWithData()
    t
  }

  def prepareDbWithData() = db.withSession {
    implicit session => {
      // load Data if needded
      // create Tables
      (Tables.Users.ddl ++ Tables.Tokens.ddl).create
    }
  }
}
