package services

import javax.inject.Inject
import connectors.MongoDbConnector
import models._

import scala.concurrent.Future

class DataService @Inject()(mongoDbConnector: MongoDbConnector){

  //C
  def createBoard(newBoard:TicTacToeBoard):Future[TicTacToeBoard] = {
    mongoDbConnector.insert(newBoard)
  }
  //R
  def readBoard(name:String):Future[Option[TicTacToeBoard]] = {
    mongoDbConnector.fetch(name)
  }
  //U
  def updateBoard(id:String, newBoard: TicTacToeBoard):Future[TicTacToeBoard] = {
    mongoDbConnector.update(id, newBoard)
  }
  //D
  def deleteBoard(id:String):Future[Boolean] = {
    mongoDbConnector.destroy(id)
  }


  /////////////////
  //custom

  def getAll: Future[List[TicTacToeBoard]] = {
    mongoDbConnector.getAll
  }

  def exists(id: String): Future[Boolean] = {
    mongoDbConnector.exists(id)
  }
}