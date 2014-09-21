package controllers.users


import models.web.{Navigation, NavigationItem, NavigationMenu}
import play.api.mvc.Controller
import play.api.libs.json._
import securesocial.core._
import play.api.libs.functional.syntax._
import models.{Tables, User}


abstract class LoginController extends Controller with SecureSocial {
  //val userDb:UserDb = new UserDb() // plugin appropriate implementation


  implicit val authenticationMethodFormat = Json.format[AuthenticationMethod]
  implicit val identityIdFormat = Json.format[IdentityId]
  implicit val oAuth1InfoFormat = Json.format[OAuth1Info]
  implicit val oAuth2InfoFormat = Json.format[OAuth2Info]
  implicit val passwordInfoFormat = Json.format[PasswordInfo]
  implicit val socialUserJsonFormat = (
    (__ \ 'uid).formatNullable[Long] and
      (__ \ 'identityId).format[IdentityId] and
      (__ \ 'firstName).format[String] and
      (__ \ 'lastName).format[String] and
      (__ \ 'fullName).format[String] and
      (__ \ 'email).formatNullable[String] and
      (__ \ 'avatarUrl).formatNullable[String] and
      (__ \ 'authenticationMethod).format[AuthenticationMethod] and
      (__ \ 'oAuth1Info).formatNullable[OAuth1Info] and
      (__ \ 'oAuth2Info).formatNullable[OAuth2Info] and
      (__ \ 'passwordInfo).formatNullable[PasswordInfo]
    )(User.apply(
    _: Option[Long],
    _: IdentityId,
    _: String,
    _: String,
    _: String,
    _: Option[String],
    _: Option[String],
    _: AuthenticationMethod,
    _: Option[OAuth1Info],
    _: Option[OAuth2Info],
    _: Option[PasswordInfo]
  ), unlift(User.unapply))


}

object Login extends LoginController {
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