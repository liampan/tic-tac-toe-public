package models

import play.api.mvc.{Request, WrappedRequest}

case class RequestWithSession[A](request: Request[A], sessionID: String) extends WrappedRequest[A](request)