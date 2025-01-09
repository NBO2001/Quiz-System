package com.nbo.utils


import play.api.Configuration
import javax.inject.{Inject, Singleton}


@Singleton
class AppConfig @Inject()(config: Configuration){

    val databaseUrl: String = config.get[String]("database.url")

}