package services

import securesocial.core._

import scala.slick.direct.AnnotationMapper.table

/**
 * Created by simipro on 9/14/14.
 */

case class YuluUser(identityId: IdentityId, firstName: String, lastName: String, fullName: String, email: Option[String],
                      avatarUrl: Option[String], authMethod: AuthenticationMethod,
                      oAuth1Info: Option[OAuth1Info] = None,
                      oAuth2Info: Option[OAuth2Info] = None,
                      passwordInfo: Option[PasswordInfo] = None) extends Identity

object YuluUser {
  def apply(i: Identity): YuluUser = {
    YuluUser(
      i.identityId, i.firstName, i.lastName, i.fullName,
      i.email, i.avatarUrl, i.authMethod, i.oAuth1Info,
      i.oAuth2Info, i.passwordInfo
    )
  }
}
