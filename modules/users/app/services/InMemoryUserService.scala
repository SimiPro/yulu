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
import securesocial.core.providers.Token
import models.Tables.{Users, Tokens}


/**
 * A Sample In Memory user service in Scala
 *
 * IMPORTANT: This is just a sample and not suitable for a production environment since
 * it stores everything in memory.
 */
class InMemoryUserService(app: play.api.Application) extends UserServicePlugin(app) {
  val logger = Logger("application.controllers.InMemoryUserService")
  val users = models.User

  override def find(id: IdentityId): Option[Identity] = Users.findByIdentityId(id)

  override def findByEmailAndProvider(email: String, providerId: String): Option[Identity] = Users.findByEmailAndProvider(email, providerId)

  override def deleteToken(uuid: String): Unit = Tokens.delete(uuid)

  override def save(user: Identity): Identity = Users.save(user)

  override def save(token: Token): Unit = Tokens.save(token)

  override def findToken(token: String): Option[Token] = Tokens.findById(token)

  override def deleteExpiredTokens(): Unit = Tokens.deleteExpiredTokens()
}


