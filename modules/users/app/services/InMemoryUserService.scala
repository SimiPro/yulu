/**
 * Copyright 2012 Jorge Aliss (jaliss at gmail dot com) - twitter: @jaliss
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package services


import akka.event.slf4j.Logger
import securesocial.core._
import securesocial.core.providers.{MailToken}
import models.Tables.{Users, Tokens}
import securesocial.core.services.{SaveMode, UserService}

import scala.concurrent.Future


/**
 * A Sample In Memory user service in Scala
 *
 * IMPORTANT: This is just a sample and not suitable for a production environment since
 * it stores everything in memory.
 */
class InMemoryUserService extends UserService[BasicProfile] {
  val logger = Logger("application.controllers.InMemoryUserService")
  val users = models.User

  /*
  override def find(id: IdentityId): Option[Identity] =

  override def findByEmailAndProvider(email: String, providerId: String): Option[Identity] = Users.findByEmailAndProvider(email, providerId)

  override def deleteToken(uuid: String): Unit = Tokens.delete(uuid)

  override def save(user: Identity): Identity = Users.save(user)

  override def save(token: Token): Unit = Tokens.save(token)

  override def findToken(token: String): Option[Token] = Tokens.findById(token)

  override def deleteExpiredTokens(): Unit = Tokens.deleteExpiredTokens()

  */
  override def find(providerId: String, userId: String): Future[Option[BasicProfile]] = Users.findByProviderAndUserId(providerId, userId)

  override def findByEmailAndProvider(email: String, providerId: String): Future[Option[BasicProfile]] = Users.findByEmailAndProvider(email, providerId)

  override def deleteToken(uuid: String): Future[Option[MailToken]] = Tokens.delete(uuid)

  override def link(current: BasicProfile, to: BasicProfile): Future[BasicProfile] = {
    //TODO: Implement later
    Future.successful(current)
  }

  override def passwordInfoFor(user: BasicProfile): Future[Option[PasswordInfo]] = {
    //TODO: Implement later
    Future.successful(user.passwordInfo)
  }

  override def save(profile: BasicProfile, mode: SaveMode): Future[BasicProfile] = Users.save(profile, mode)

  override def findToken(token: String): Future[Option[MailToken]] = Tokens.findByIdFuture(token)

  override def deleteExpiredTokens(): Unit = Tokens.deleteExpiredTokens()

  override def updatePasswordInfo(user: BasicProfile, info: PasswordInfo): Future[Option[BasicProfile]] = Users.updatePasswordInfo(user, info)

  override def saveToken(token: MailToken): Future[MailToken] = Tokens.saveWithFuture(token)
}


