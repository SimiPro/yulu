package controllers.users


import play.api.Logger
import play.api.i18n.Messages
import play.twirl.api.HtmlFormat
import securesocial.controllers.{MailTokenBasedOperations, Registration, BaseLoginPage}
import securesocial.core.providers.UsernamePasswordProvider
import securesocial.core.{SecureSocial, BasicProfile, RuntimeEnvironment}
import play.api.mvc.Action

import scala.concurrent.Future

/**
 * Created by simipro on 25/09/14.
 */
class SignUpController(override implicit val env: RuntimeEnvironment[BasicProfile]) extends Registration {



 override def handleStartSignUp = Action.async {
    implicit request =>
      Logger.info("handleStartSignUp")
      startForm.bindFromRequest.fold(
        errors => {
          Logger.info("BadRequest from SignUp: " + startForm)
          Future.successful(BadRequest(HtmlFormat.raw("BadRequest")))
        },
        e => {
          val email = e.toLowerCase
          // check if there is already an account for this email address
          import scala.concurrent.ExecutionContext.Implicits.global
          env.userService.findByEmailAndProvider(email, UsernamePasswordProvider.UsernamePassword).map {
            maybeUser =>
              maybeUser match {
                case Some(user) =>
                  // user signed up already, send an email offering to login/recover password
                  env.mailer.sendAlreadyRegisteredEmail(user)
                case None =>
                  import scala.concurrent.ExecutionContext.Implicits.global
                  createToken(email, isSignUp = true).flatMap { token =>
                    env.mailer.sendSignUpEmail(email, token.uuid)
                    env.userService.saveToken(token)
                  }
              }
              handleStartResult().flashing(Success -> Messages("THANK YOU CHECK EMAIL"), Email -> email)
          }
        }
      )
  }

  /**
   * Starts the sign up process
   */
  override def startSignUp = Action {
    implicit request =>
      if (SecureSocial.enableRefererAsOriginalUrl) {
        SecureSocial.withRefererAsOriginalUrl(Ok(env.viewTemplates.getStartSignUpPage(startForm)))
      } else {
        Ok(env.viewTemplates.getStartSignUpPage(startForm))
      }
  }

}
