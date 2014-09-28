package controllers.users


import models.web.{Navigation, NavigationItem, NavigationMenu}
import play.api.Logger
import play.api.mvc.{RequestHeader, Action, AnyContent}
import play.api.libs.json._
import securesocial.controllers.BaseLoginPage
import securesocial.core._
import play.api.libs.functional.syntax._
import models.{Tables, User}
import securesocial.core.services.RoutesService


class LoginController(override implicit val env: RuntimeEnvironment[BasicProfile]) extends BaseLoginPage[BasicProfile]{
  //val userDb:UserDb = new UserDb() // plugin appropriate implementation

  override def login: Action[AnyContent] = {
    //
    Logger.info("using custom login controller")
    super.login
  }


  implicit val authenticationMethodFormat = Json.format[AuthenticationMethod]
  implicit val oAuth1InfoFormat = Json.format[OAuth1Info]
  implicit val oAuth2InfoFormat = Json.format[OAuth2Info]
  implicit val passwordInfoFormat = Json.format[PasswordInfo]
  implicit val UserJsonFormat = (
      (__ \ 'providerId).format[String] and
      (__ \ 'userId).format[String] and
      (__ \ 'firstName).formatNullable[String] and
      (__ \ 'lastName).formatNullable[String] and
      (__ \ 'fullName).formatNullable[String] and
      (__ \ 'email).formatNullable[String] and
      (__ \ 'avatarUrl).formatNullable[String] and
      (__ \ 'authenticationMethod).format[AuthenticationMethod] and
      (__ \ 'oAuth1Info).formatNullable[OAuth1Info] and
      (__ \ 'oAuth2Info).formatNullable[OAuth2Info] and
      (__ \ 'passwordInfo).formatNullable[PasswordInfo]
    )(BasicProfile.apply(
    _: String,
    _: String,
    _: Option[String],
    _: Option[String],
    _: Option[String],
    _: Option[String],
    _: Option[String],
    _: AuthenticationMethod,
    _: Option[OAuth1Info],
    _: Option[OAuth2Info],
    _: Option[PasswordInfo]
  ), unlift(BasicProfile.unapply))

  def getUsers = SecuredAction {
    val allUsers = Tables.Users.all
    Ok(Json.toJson(allUsers))
  }

  def navigation() = SecuredAction {
    val menus = Seq(
      NavigationMenu(Seq(
        NavigationItem("Change Password", "#/password/change")
      ), position = "left"
      ),
      NavigationMenu(Seq(
        NavigationItem("Sign Out", "#sighnout")
      ), position = "right")
    )
    val navigation = Navigation("default", menus)
    Ok(Navigation.toJson(navigation))
  }



}

class CustomRoutesService extends RoutesService.Default {
  override def loginPageUrl(implicit req: RequestHeader): String = controllers.users.routes.LoginController.login().absoluteURL(IdentityProvider.sslEnabled)
}
