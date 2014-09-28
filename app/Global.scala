import java.lang.reflect.Constructor

import controllers.users.CustomRoutesService
import play.api.Play.current
import models.Tables.{Tokens, Users}
import play.api.db.DB
import play.api.{GlobalSettings, Logger}
import securesocial.core._
import _root_.services.InMemoryUserService

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
        Users.save(
       BasicProfile(
         "providerId",
         "userId",
         Some("firstname"),
         Some("lastname"),
         Some("fullname"),
         Some("email"),
         Some("avatarUrl"),
         AuthenticationMethod("oauth2"),
         Some(OAuth1Info("token", "secret")),
         Some( OAuth2Info("accesToken", Some("tokenType"),Some(12),Some("refreshToken"))),
         Some(PasswordInfo("hasher", "password",Some("salt")))
         ))
      }
    }
  }
  /**
   * The runtime environment for this sample app.
   */
  object MyRuntimeEnvironment extends RuntimeEnvironment.Default[BasicProfile] {
    override lazy val routes = new CustomRoutesService()
    override lazy val userService: InMemoryUserService = new InMemoryUserService()
    //override lazy val eventListeners = List(new MyEventListener())
  }

  /**
   * An implementation that checks if the controller expects a RuntimeEnvironment and
   * passes the instance to it if required.
   *
   * This can be replaced by any DI framework to inject it differently.
   *
   * @param controllerClass
   * @tparam A
   * @return
   */
  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    val instance = controllerClass.getConstructors.find { c =>
      val params = c.getParameterTypes
      params.length == 1 && params(0) == classOf[RuntimeEnvironment[BasicProfile]]
    }.map {
      _.asInstanceOf[Constructor[A]].newInstance(MyRuntimeEnvironment)
    }
    instance.getOrElse(super.getControllerInstance(controllerClass))
  }
}
