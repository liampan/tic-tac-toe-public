package modules

import connectors.{MongoDbConnector, MongoDbConnectorImpl}
import play.api.{Configuration, Environment}
import com.google.inject.AbstractModule
import controllers.TttGameLogic

class MyModule(
              environment: Environment,
              configuration: Configuration) extends AbstractModule {

  override def configure =
    bind(classOf[MongoDbConnector]).to(classOf[MongoDbConnectorImpl])
}