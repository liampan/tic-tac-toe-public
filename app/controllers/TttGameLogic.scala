package controllers

import models._

class TttGameLogic {

  def playMove(game: TicTacToeBoard, index: Int): Option[TicTacToeBoard] = {
    val nb: Option[Board] = game.board.updateIndex(index, playerState(game.whosTurn))
    nb.map(game.playMoveAndUpdateBoard)
  }

  def checkWin(game: TicTacToeBoard): Boolean = {

    def lineCheck(lines: Seq[List[Int]]):Boolean ={
      lines.map(ls => ls.sum).foldLeft(false)((bool, sum) => bool || sum == 3*Naught || sum == 3*Cross)
    }

    val squares: List[Int] = game.board.squaresStates.map(StateToInt)

    val horizontals = squares.sliding(3,3).toList
    val verticals = horizontals.transpose
    val diagonals = Seq(List(squares.head,squares(4),squares(8)),List(squares(2),squares(4),squares(6)))

    lineCheck(horizontals) || lineCheck(verticals) || lineCheck(diagonals)
  }


  private def playerState(i: Int): State = {
    i match {
      case 1 => Cross
      case 2 => Naught
    }
  }

  import scala.language.implicitConversions
  private implicit def StateToInt(state: State): Int = {
    state match {
      case Empty => 0
      case Naught => -1
      case Cross => 1
    }
  }

}