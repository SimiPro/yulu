package models

import core.WithDbData
import org.specs2.mutable.Specification
import securesocial.core.{OAuth1Info, OAuth2Info, AuthenticationMethod, IdentityId}


/**
 * Created by simipro on 9/20/14.
 */


class UserSpec extends Specification {

  "The 'Hello world' string" should {
    "contain 11 characters" in new WithDbData {
      Tables.Users.save(User(
        None,
        IdentityId.apply("userId", "providerId"),
        "firstName",
        "lastName",
        "fullName",
        Some("user@user.com"),
        Some("avatarUrl"),
        AuthenticationMethod("OAUTH2"),
        Some(OAuth1Info.apply("token", "secret")),
        Some(OAuth2Info.apply("accessToken", Some("tokenType"), Some(1), Some("refreshToken")))
      ))

      var user = Tables.Users.findByIdentityId(IdentityId.apply("userId", "providerId"))

      user match {
        case Some(user) => {
          user.lastName === "lastName"
          user.firstName === "firstName"
          user.fullName === "fullName"
          user.email.get === "user@user.com"
          user.avatarUrl.get === "avatarUrl"
          user.oAuth1Info.get.token === "token"
          user.oAuth2Info.get.accessToken === "accessToken"
        }
        case None => throwA[Exception]("User not found Exception")
      }
    }


    "start with 'Hello'" in {
      "Hello world" must startWith("Hello")
    }
    "end with 'world'" in {
      "Hello world" must endWith("world")
    }
  }

}
