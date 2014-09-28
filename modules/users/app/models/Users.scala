package models



import securesocial.core._
import securesocial.core.services.SaveMode
import scala.concurrent.Future
import scala.slick.lifted.ProvenShape
import scala.slick.driver.H2Driver.simple._
import securesocial.core.providers.MailToken
import org.joda.time.DateTime
import play.api.Play.current
import play.api.db.DB

// need for Mapping Date to org.joda.Datetime

import com.github.tototoshi.slick.H2JodaSupport._



class DUser(u: User) extends BasicProfile(u.providerId, u.userId, u.firstName, u.lastName, u.fullName, u.email, u.avatarUrl, u.authMethod, u.oAuth1Info, u.oAuth2Info, u.passwordInfo) {

}

case class User(
                 uid: Option[Long] = None,
                 providerId: String,
                 userId: String,
                 firstName: Option[String],
                 lastName: Option[String],
                 fullName: Option[String],
                 email: Option[String],
                 avatarUrl: Option[String],
                 authMethod: AuthenticationMethod,
                 oAuth1Info: Option[OAuth1Info],
                 oAuth2Info: Option[OAuth2Info],
                 passwordInfo: Option[PasswordInfo] = None) extends GenericProfile

object UserToBasicUserProfile {
  def apply(i: User): BasicProfile = BasicProfile(
    i.providerId, i.userId,
    i.firstName, i.lastName,
    i.fullName, i.email,
    i.avatarUrl, i.authMethod,
    i.oAuth1Info, i.oAuth2Info,
    i.passwordInfo)
}


class Users(tag: Tag) extends Table[BasicProfile](tag, "user") {

  implicit def string2AuthenticationMethod = MappedColumnType.base[AuthenticationMethod, String](
    authenticationMethod => authenticationMethod.method,
    string => AuthenticationMethod(string))

  implicit def tuple2OAuth1Info(tuple: (Option[String], Option[String])): Option[OAuth1Info] = tuple match {
    case (Some(token), Some(secret)) => Some(OAuth1Info(token, secret))
    case _ => None
  }

  implicit def tuple2OAuth2Info(tuple: (Option[String], Option[String], Option[Int], Option[String])): Option[OAuth2Info] = tuple match {
    case (Some(token), tokenType, expiresIn, refreshToken) => Some(OAuth2Info(token, tokenType, expiresIn, refreshToken))
    case _ => None
  }

  implicit def tuple2PasswordInfo(tuple: (Option[String],Option[String],Option[String])): Option[PasswordInfo] = tuple match {
    case (Some(hasher), Some(password), salt ) => Some(PasswordInfo(hasher, password, salt))
    case _ => None
  }

  def * : ProvenShape[BasicProfile] = {
    val shapedValue = (
      userId,
      providerId,
      firstName,
      lastName,
      fullName,
      email,
      avatarUrl,
      authMethod,
      token,
      secret,
      accessToken,
      tokenType,
      expiresIn,
      refreshToken,
      hasher,
      password,
      salt).shaped

    shapedValue.<>({
      tuple =>
        BasicProfile.apply(
          userId = tuple._1,
          providerId = tuple._2,
          firstName = tuple._3,
          lastName = tuple._4,
          fullName = tuple._5,
          email = tuple._6,
          avatarUrl = tuple._7,
          authMethod = tuple._8,
          oAuth1Info = (tuple._9, tuple._10),
          oAuth2Info = (tuple._11, tuple._12, tuple._13, tuple._14),
          passwordInfo = (tuple._15, tuple._16, tuple._17)
        )
    }, {
      (u: BasicProfile) =>
        Some {
          (
            u.userId,
            u.providerId,
            u.firstName,
            u.lastName,
            u.fullName,
            u.email,
            u.avatarUrl,
            u.authMethod,
            u.oAuth1Info.map(_.token),
            u.oAuth1Info.map(_.secret),
            u.oAuth2Info.map(_.accessToken),
            u.oAuth2Info.flatMap(_.tokenType),
            u.oAuth2Info.flatMap(_.expiresIn),
            u.oAuth2Info.flatMap(_.refreshToken),
            u.passwordInfo.map(_.hasher),
            u.passwordInfo.map(_.password),
            u.passwordInfo.flatMap(_.salt))
        }
    })
  }

  /* To delete cause of scala 2.11 SocialSecure 2.11
    implicit def tuple2IdentityId(tuple: (String, String)): IdentityId = tuple match {
      case (userId, providerId) => IdentityId(userId, providerId)
    }

  */
  //def uid = column[Long]("id", O.PrimaryKey, O.AutoInc)

  def userId = column[String]("userId", O.PrimaryKey)

  def providerId = column[String]("providerId")

  def email = column[Option[String]]("email")

  def firstName = column[Option[String]]("firstName")

  def lastName = column[Option[String]]("lastName")

  def fullName = column[Option[String]]("fullName")

  def authMethod = column[AuthenticationMethod]("authMethod")

  def avatarUrl = column[Option[String]]("avatarUrl")

  // oAuth 1
  def token = column[Option[String]]("token")

  def secret = column[Option[String]]("secret")

  // oAuth 2
  def accessToken = column[Option[String]]("accessToken")

  def tokenType = column[Option[String]]("tokenType")

  def expiresIn = column[Option[Int]]("expiresIn")

  def refreshToken = column[Option[String]]("refreshToken")

  // passwordInfo
  def hasher = column[Option[String]]("hasher")

  def password = column[Option[String]]("password")

  def salt = column[Option[String]]("salt")

}

class Tokens(tag: Tag) extends Table[MailToken](tag, "token") {

  def * : ProvenShape[MailToken] = {
    val shapedValue = (uuid, email, creationTime, expirationTime, isSignUp).shaped

    shapedValue.<>({
      tuple =>
        MailToken(
          uuid = tuple._1,
          email = tuple._2,
          creationTime = tuple._3,
          expirationTime = tuple._4,
          isSignUp = tuple._5)
    }, {
      (t: MailToken) =>
        Some {
          (
            t.uuid,
            t.email,
            t.creationTime,
            t.expirationTime,
            t.isSignUp)
        }
    })
  }

  def uuid = column[String]("uuid")

  def email = column[String]("email")

  def creationTime = column[DateTime]("creationTime")

  def expirationTime = column[DateTime]("expirationTime")

  def isSignUp = column[Boolean]("isSignUp")
}

trait WithDefaultSession {

  def withSession[T](block: (Session => T)) = {
    lazy val database = Database.forDataSource(DB.getDataSource())
    database withSession {
      session =>
        block(session)
    }
  }

}

object Tables extends WithDefaultSession {

  val Tokens = new TableQuery[Tokens](new Tokens(_)) {

    def findByIdFuture(tokenId: String): Future[Option[MailToken]] = withSession {
      implicit session => {
        Future.successful(findById(tokenId))
      }
    }

    def findById(tokenId: String): Option[MailToken] = withSession {
      implicit session =>
        val q = for {
          token <- this
          if token.uuid === tokenId
        } yield token

        q.firstOption
    }

    def saveWithFuture(token:MailToken):Future[MailToken] = withSession {
      implicit session => {
        Future.successful(save(token))
      }
    }

    def save(token: MailToken): MailToken = withSession {
      implicit session =>
        findById(token.uuid) match {
          case None => {
            this.insert(token)
            token
          }
          case Some(existingToken) => {
            val tokenRow = for {
              t <- this
              if t.uuid === existingToken.uuid
            } yield t

            val updatedToken = token.copy(uuid = existingToken.uuid)
            tokenRow.update(updatedToken)
            updatedToken
          }
        }
    }

    def delete(uuid: String) = withSession {
      implicit session =>
        val q = for {
          t <- this
          if t.uuid === uuid
        } yield t
        var result = q.firstOption
        q.delete
        Future.successful(result)
    }

    def deleteExpiredTokens(): Unit = {
      deleteExpiredTokens(DateTime.now())
    }

    def deleteExpiredTokens(currentDate: DateTime) = withSession {
      implicit session =>
        val q = for {
          t <- this
          if t.expirationTime < currentDate
        } yield t

        q.delete
    }

  }

  val Users = new TableQuery[Users](new Users(_)) {
    def autoInc = this returning this.map(_.userId)

    def findById(id: String) = withSession {
      implicit session =>
        val q = for {
          user <- this
          if user.userId === id
        } yield user

        q.firstOption
    }

    def findByEmailAndProvider(email: String, providerId: String): Future[Option[BasicProfile]] = withSession {
      implicit session =>
        val q = for (
          query <- this.filter(x => x.email === email && x.providerId === providerId)
        ) yield {
          query
        }
        Future.successful(q.firstOption)
    }


    def findByProviderAndUserId(providerId: String, userId: String): Future[Option[BasicProfile]] = withSession {
      implicit session =>{
        Future.successful(findByProviderAndUserIdOption(providerId, userId))
      }
    }

    def findByProviderAndUserIdOption(providerId: String, userId: String): Option[BasicProfile] = withSession {
      implicit session =>
        val result = for (
          r <- this.filter(x => x.userId === userId && x.providerId === providerId)
        )
        yield {
          r
        }
        result.firstOption
    }




    def all = withSession {
      implicit session =>
        this.list
    }


    def updatePasswordInfo(user: BasicProfile, info: PasswordInfo): Future[Option[BasicProfile]] = withSession {
      implicit session => {
        val resultRow = for (
          u <- this.filter(U => U.userId === user.userId)
        ) yield {
          (u.hasher, u.password, u.salt)
        }
        resultRow.update((Some(info.hasher), Some(info.password), info.salt))
        //TODO: Mby load updated user?
        Future.successful(Some(user))
      }
    }


    def save(i: BasicProfile, saveMode: SaveMode): Future[BasicProfile] = Future.successful(i)

    def save(user: BasicProfile): BasicProfile = withSession {
      implicit session =>
        findByProviderAndUserIdOption(user.providerId, user.userId) match {
          case None => {
            val uuid = this.insert(user)
            // TODO: Bad implemented, just update uid and return new saved user
           /* val userRow = for {
              u <- this
              if u.userId === uuid
            } yield u
            */
           user
          }
          case Some(existingUser) => {
            val userRow = for {
              u <- this
              if u.userId === existingUser.userId
            } yield u
            userRow.update(user)
            user
          }
        }
    }
  }


}
