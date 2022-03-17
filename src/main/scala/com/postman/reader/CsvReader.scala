package com.postman.reader

import com.postman.utils.Constants
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.util.Properties

case class CsvReader(implicit spark: SparkSession) extends SourceReader {
  override def getDataFrameFromFile(path: String): DataFrame = spark.read.format(Constants.Csv).option("header", "true").load(path)
  override def getDataFrameFromJdbc(connectionProperties: Properties, tableName: String): DataFrame = ???
}
