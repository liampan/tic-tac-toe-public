package repositories

import conf.ApplicationConfig
import javax.inject.{Inject, Singleton}
import logger.TicTacToeLogger
import models.TicTacToeBoard
import play.modules.reactivemongo.ReactiveMongoComponent
import reactivemongo.api.indexes.{Index, IndexType}
import reactivemongo.bson.{BSONDocument, BSONObjectID}
import uk.gov.hmrc.mongo.ReactiveRepository
import uk.gov.hmrc.mongo.json.ReactiveMongoFormats


@Singleton
class TicTacToeBoardRepository @Inject()(config: ApplicationConfig,
                                         reactiveMongoComponent: ReactiveMongoComponent,
                                         logger: TicTacToeLogger)
  extends ReactiveRepository[TicTacToeBoard, BSONObjectID](
    collectionName = config.collectionName,
    mongo = reactiveMongoComponent.mongoConnector.db,
    domainFormat = TicTacToeBoard.formats,
    idFormat = ReactiveMongoFormats.objectIdFormats
  ) {


  //TTL
  override def indexes: Seq[Index] = {
    logger.info("setting index expiresAt")
    Seq(
      Index(
        Seq("expiresAt" -> IndexType.Ascending),
        name = Some("expiresAt"),
        options = BSONDocument("expireAfterSeconds" -> config.expireAfterSeconds)
      )
    )
  }

}

