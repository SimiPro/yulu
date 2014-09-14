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


/**
 * A Sample In Memory user service in Scala
 *
 * IMPORTANT: This is just a sample and not suitable for a production environment since
 * it stores everything in memory.
 */
class InMemoryUserService extends UserService {
  val logger = Logger("application.controllers.InMemoryUserService")
  val userDB = 

  override def find(id: IdentityId): Option[Identity] = {


  }

  override def findByEmailAndProvider(email: String, providerId: String): Option[Identity] = ???

  override def deleteToken(uuid: String): Unit = ???

  override def save(user: Identity): Identity = ???

  override def save(token: Token): Unit = ???

  override def findToken(token: String): Option[Token] = ???

  override def deleteExpiredTokens(): Unit = ???
}


