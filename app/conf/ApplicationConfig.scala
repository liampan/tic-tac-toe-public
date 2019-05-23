package conf

import javax.inject._
import play.api.{Configuration, Environment}

@Singleton
class ApplicationConfig @Inject()(
                                 configuration: Configuration,
                                 environment: Environment
                                 ) {

  private def loadConfig(key: String): String = {
    configuration.get[String](key)
  }

  lazy val appName: String = loadConfig("appName")
  lazy val collectionName: String = loadConfig("mongodb.collection")
  lazy val mongoUri: String = loadConfig("mongodb.uri")
  lazy val env: String = loadConfig("ENV")
  lazy val expireAfterSeconds: Int = loadConfig("mongodb.expireAfterSeconds").toInt
  lazy val appHost = loadConfig("appHost")
}
