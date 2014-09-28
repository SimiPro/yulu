package controllers

import models.web.{Navigation, NavigationItem, NavigationMenu}
import play.api.Logger
import play.api.mvc.{Action}
import securesocial.core.{RuntimeEnvironment, SecureSocial}

import scala.slick.profile.BasicProfile

class Application(override implicit val env: RuntimeEnvironment[BasicProfile]) extends  SecureSocial[BasicProfile]{


  def index = Action {
    Ok(views.html.index())
  }

  def navigation() = Action {
    val menus =
      Seq(
        NavigationMenu(
          Seq(
            NavigationItem("Sign Up", "#/signup"),
            NavigationItem("Sign In", "#/login")
          ),
          position = "left"
        )
      )

    val navigation = Navigation("default", menus)

    Ok(Navigation.toJson(navigation))
  }

}