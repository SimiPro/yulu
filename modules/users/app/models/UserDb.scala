package models


import securesocial.core._
import services.YuluUser

import scala.slick.direct.AnnotationMapper.{column, table}

/**
 * Created by simipro on 9/14/14.
 */
class UserDb {
  def findAll: List[YuluUser] = ???

}

@table("user")
case class User(
                @column("id")id:Long,
                @column("identity_id")identityId: IdentityId,
                @column("firstname")firstName: String,
                @column("lastname")lastName: String,
                @column("fullname")fullName: String,
                @column("email")email: Option[String],
                @column("avatarurl")avatarUrl: Option[String],
                @column("authmethod")authMethod: AuthenticationMethod,
                @column("oauth1info")oAuth1Info: Option[OAuth1Info] = None,
                @column("oauth2info")oAuth2Info: Option[OAuth2Info] = None,
                @column("passwordinfo")passwordInfo: Option[PasswordInfo] = None
                 ) {

}
