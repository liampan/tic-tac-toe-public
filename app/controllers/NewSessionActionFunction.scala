package controllers

import com.google.inject.Inject
import models.RequestWithSession
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}

class NewSessionActionFunction @Inject()(
                                          val parser: BodyParsers.Default
                                        )(implicit val executionContext: ExecutionContext) extends ActionFunction[Request, RequestWithSession] with ActionBuilder[RequestWithSession, AnyContent] {

  override def invokeBlock[A](request: Request[A], block: RequestWithSession[A] => Future[Result]): Future[Result] = {
    val newSessionID = java.util.UUID.randomUUID().toString

    block(RequestWithSession(request, newSessionID))
      .map(
      _.withNewSession
        .withSession("uuid" -> newSessionID)
    )


  }

}
