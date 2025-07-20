name := """backend"""
organization := "com.nbo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.15"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "2.9.0"

libraryDependencies += "com.typesafe.akka" %% "akka-actor-typed" % "2.6.20" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.6.20" % Test
libraryDependencies += "com.typesafe.play" %% "play-test" % "2.8.20" % Test
libraryDependencies += "com.typesafe.play" %% "play-specs2" % "2.8.20" % Test


// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.nbo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.nbo.binders._"
