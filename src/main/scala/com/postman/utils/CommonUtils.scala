package com.postman.utils

import com.postman.exception.{UnSupportedSinkException, UnSupportedSourceException}
import com.postman.reader.{CsvReader, JdbcReader, SourceReader}
import com.postman.sink.{JdbcSink, SinkTrait}
import org.apache.spark.sql.SparkSession

object CommonUtils {

  def getReaderFromSourceType(sourceType: String)(implicit spark: SparkSession): SourceReader = {
    sourceType match {
      case Constants.Csv => CsvReader()
      case Constants.Jdbc => JdbcReader()
      case _ => throw UnSupportedSourceException(s"sourceType type not supported ${sourceType}")
    }
  }

  def getSinkFromSinkType(implicit spark: SparkSession): SinkTrait = {
    val sinkType = spark.conf.get("spark.input.sinkType", "jdbc")
    sinkType match {
      case Constants.Jdbc => JdbcSink()
      case _ => throw UnSupportedSinkException(s"sink type not supported ${sinkType}")
    }
  }
}
