package com.postman.reader

import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

case class JdbcReader(implicit spark: SparkSession) extends SourceReader {

  override def getDataFrameFromFile(path: String): DataFrame = ???

  override def getDataFrameFromJdbc(connectionProperties: Properties, tableName: String): DataFrame = {
    val jdbcUrl = connectionProperties.getProperty("jdbcUrl")
    spark.read.jdbc(jdbcUrl, tableName, connectionProperties)
  }
}
