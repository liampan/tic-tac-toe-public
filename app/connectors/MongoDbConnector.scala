package connectors

import conf.ApplicationConfig
import javax.inject.{Inject, Singleton}
import models.TicTacToeBoard
import logger.TicTacToeLogger
import play.api.libs.json.Json
import reactivemongo.play.json._
import repositories.TicTacToeBoardRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class MongoDbConnectorImpl @Inject()(config: ApplicationConfig,
                                     ticTacToeBoardRepository: TicTacToeBoardRepository,
                                     logger: TicTacToeLogger
                                    ) extends MongoDbConnector {

  override def getAll: Future[List[TicTacToeBoard]] = {
    logger.info("getting all from db")
    ticTacToeBoardRepository.findAll()
  }

  override def insert(board: TicTacToeBoard): Future[TicTacToeBoard] = {
    logger.info(s"inserting ${board.id} to db")
    ticTacToeBoardRepository.insert(board).map(wr => {logger.info(wr); board})
  }

  override def fetch(name: String): Future[Option[TicTacToeBoard]] = {
    logger.info(s"fetch $name from db")
    ticTacToeBoardRepository.find("id" -> name).map(_.headOption)
  }

  override def update(id: String, updatedBoard: TicTacToeBoard): Future[TicTacToeBoard] = {
    logger.info(s"update $id turn: ${updatedBoard.whosTurn}")
    ticTacToeBoardRepository.collection.update(ordered = false)
      .one(Json.obj("id" -> id), updatedBoard, upsert=true)
      .map(uwr => {logger.info(uwr); updatedBoard})
  }

  override def destroy(id: String): Future[Boolean] = {
    logger.info(s"deleted $id from db")
    ticTacToeBoardRepository.collection.delete.one(Json.obj("id" -> id)).map(wr => {logger.info(wr); true})
  }

  override def exists(id: String): Future[Boolean] = {
    logger.info(s"checking for $id")
    ticTacToeBoardRepository.find("id" -> id).map(_.nonEmpty)
  }

}

trait MongoDbConnector{
  def getAll: Future[List[TicTacToeBoard]]
  def insert(value: TicTacToeBoard):Future[TicTacToeBoard]
  def fetch(id: String):Future[Option[TicTacToeBoard]]
  def update(id: String, updatedPlayer:TicTacToeBoard):Future[TicTacToeBoard]
  def destroy(id: String):Future[Boolean]
  def exists(id: String): Future[Boolean]
}