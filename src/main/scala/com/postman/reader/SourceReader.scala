package com.postman.reader

import org.apache.spark.sql.DataFrame

import java.util.Properties

trait SourceReader extends Serializable {
  def getDataFrameFromFile(path: String): DataFrame
  def getDataFrameFromJdbc(connectionProperties: Properties, tableName: String): DataFrame
}
