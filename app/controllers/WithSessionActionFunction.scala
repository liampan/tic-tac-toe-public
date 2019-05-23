package controllers

import com.google.inject.Inject
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import models.RequestWithSession

class WithSessionActionFunction @Inject()(
                       val parser: BodyParsers.Default
                     )(implicit val executionContext: ExecutionContext) extends ActionFunction[Request, RequestWithSession] with ActionBuilder[RequestWithSession, AnyContent] {

  override def invokeBlock[A](request: Request[A], block: RequestWithSession[A] => Future[Result]): Future[Result] = {
    val optSessionID = request.session.get("uuid")
    val sessionID = optSessionID.getOrElse(java.util.UUID.randomUUID().toString)
    block(RequestWithSession(request, sessionID)).map {
      res =>
        optSessionID.fold(res.withSession("uuid" -> sessionID))(_ => res)
    }
  }
}
