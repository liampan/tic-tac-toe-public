name := """tic-tac-toe"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.12"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  "org.mongodb" % "mongodb-driver" % "3.6.0",
  ws
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.5.0"

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-bson" % "2.5.0"

resolvers += Resolver.bintrayRepo("hmrc", "releases")

libraryDependencies ++= Seq(
  "uk.gov.hmrc" %% "simple-reactivemongo" % "7.19.0-play-26"
)

libraryDependencies += guice
