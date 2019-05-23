package controllers

import com.google.inject.Inject
import conf.ApplicationConfig
import models._
import play.api.mvc._
import services.DataService
import views.html._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Random

class Application @Inject()(
                             appConfig: ApplicationConfig,
                             dataService: DataService,
                             gameLogic: TttGameLogic,
                             viewer: boardShell,
                             joinView: joinGameView,
                             WithSession: WithSessionActionFunction,
                             NewSession: NewSessionActionFunction
                           ) extends InjectedController {

  def index(message: Option[String]) = WithSession {
    Ok(views.html.index(message))
  }

  def newGame(gid: Option[String]) = NewSession.async {
    request =>
      val newGameID = gid.map(_.replaceAll("[\\W]|_", "")) match {
        case Some(s) if s != "" => s
        case _ => "game-" + (Random.nextInt(899)+100)
      }
      dataService.exists(newGameID).flatMap{ exists =>
        if (!exists) {
        val newgb = dataService.createBoard(TicTacToeBoard(newGameID, Player("Player 1", request.sessionID)))
        newgb.map(ng => Redirect(controllers.routes.Application.joinGame(Some(ng.id))))
      }
        else Future.successful(Redirect(controllers.routes.Application.index(Some("that board already exists"))))
      }
  }

  def joinGame(optGameID: Option[String]) = WithSession.async {
    implicit request =>
      optGameID match {
        case Some(gameID) => {
          dataService.readBoard(gameID).map {
            case None => Redirect(controllers.routes.Application.index(Some(s"'$gameID' was not found")))
            case Some(b) => {
              if (b.P2.nonEmpty)
                Redirect(controllers.routes.Application.viewGame(gameID))
              else if (b.P1.sessionID != request.sessionID) {
                dataService.updateBoard(gameID, b.copy(P2 = Some(Player("Player 2", request.sessionID))))
                Redirect(controllers.routes.Application.joinGame(Some(gameID)))
              }
              else
                Ok(joinView(gameID))
            }
          }
        }
        case None => Future.successful(Redirect(controllers.routes.Application.index(Some("there was an error"))))
      }
  }

  def updateGame(gameID: String, index: Int) = WithSession.async {
    implicit request =>
      dataService.readBoard(gameID).flatMap {
        case Some(gb) => {
          if (request.sessionID == gb.whosSession) {
            gameLogic.playMove(gb, index) match {
              case Some(nb) => {
                dataService.updateBoard(gameID, nb).map {
                  unb =>
                    Redirect(controllers.routes.Application.viewGame(unb.id))
                }
              }
              case None => Future.successful(BadRequest(viewer(gb, "you cant play there")))
            }
          } else if(gb.allowedSessions.contains(request.sessionID)){Future.successful(Ok(viewer(gb)))}
          else Future.successful(BadRequest("this isn't your game"))
        }
        case None => Future.successful(NotFound("board not found"))
      }
  }

  def viewGame(gameID: String) = WithSession.async {
    implicit request =>
    dataService.readBoard(gameID).map {
      case None    => NotFound("game not found")
      case Some(b) => Ok(viewer(b))
    }
  }

  //////////////////////////////////////
  // helper methods while developing

  def testview = WithSession {
    implicit request =>
        Ok(viewer(TicTacToeBoard("testgame",
          Player("player 1", "test"),
          Some(Player("player 2", "abc123")),
          board = Board(_1 = Cross, _2 = Cross, _3 = Cross, _5 = Naught))))
  }

  def showAll = Action.async {
    dataService.getAll.map(b => Ok(b.mkString("\n")))
  }

  def drop(gameId: String) = Action.async {
    dataService.deleteBoard(gameId).map(d =>
      Ok(s"$gameId deleted: $d"))
  }

}
