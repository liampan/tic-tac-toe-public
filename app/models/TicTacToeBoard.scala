package models

import org.joda.time.DateTime
import play.api.libs.json._
import uk.gov.hmrc.mongo.json.ReactiveMongoFormats

case class TicTacToeBoard(id: String,
                          P1: Player,
                          P2: Option[Player] = None,
                          gameOver: Boolean = false,
                          private val turn: Int = 0,
                          expiresAt : DateTime = DateTime.now().plusHours(1),
                          board: Board = Board()
                         ){
  def whosTurn ={
    (turn % 2)+1
  }

  def prevTurn ={
    whosTurn match {
      case 1 => 2
      case 2 => 1
    }
  }

  def playMoveAndUpdateBoard(newBoard: Board) = {
    this.copy(turn = turn+1, board = newBoard)
  }

  def whosSession ={
    whosTurn match {
      case 1 => P1.sessionID
      case 2 => P2.get.sessionID
    }
  }

  def allowedSessions ={
    List(P1.sessionID) ++ P2.map(p => p.sessionID)
  }

}

case class Player(name: String, sessionID: String)

case class State(view: String)
object Cross extends State("X")
object Naught extends State("O")
object Empty extends State("-")

case class Board(
   _1 : State = Empty,
   _2 : State = Empty,
   _3 : State = Empty,
   _4 : State = Empty,
   _5 : State = Empty,
   _6 : State = Empty,
   _7 : State = Empty,
   _8 : State = Empty,
   _9 : State = Empty){

  def squaresStates: List[State] = {
    List[State](_1, _2, _3, _4, _5, _6, _7, _8, _9)
  }

  def updateIndex(index: Int, newState: State): Option[Board] = {
     // squaresStates(index) == Empty
    index match {
      case 1 if this._1 == Empty => Some(this.copy(_1 = newState))
      case 2 if this._2 == Empty => Some(this.copy(_2 = newState))
      case 3 if this._3 == Empty => Some(this.copy(_3 = newState))
      case 4 if this._4 == Empty => Some(this.copy(_4 = newState))
      case 5 if this._5 == Empty => Some(this.copy(_5 = newState))
      case 6 if this._6 == Empty => Some(this.copy(_6 = newState))
      case 7 if this._7 == Empty => Some(this.copy(_7 = newState))
      case 8 if this._8 == Empty => Some(this.copy(_8 = newState))
      case 9 if this._9 == Empty => Some(this.copy(_9 = newState))
      case _                     => None
    }
  }
}

case class x(y: List[String]) {
  require(y.length == 9)
}

object TicTacToeBoard {
  implicit val dateFormat: Format[DateTime] = ReactiveMongoFormats.dateTimeFormats
  implicit val formats: OFormat[TicTacToeBoard] = Json.format[TicTacToeBoard]
  implicit val writes: OWrites[TicTacToeBoard] = Json.writes[TicTacToeBoard]
}

object Board {
  implicit val formats: OFormat[Board] = Json.format[Board]
}

object State {
  implicit val formats: OFormat[State] = Json.format[State]
}

object Player{
  implicit val formats: OFormat[Player] = Json.format[Player]
}