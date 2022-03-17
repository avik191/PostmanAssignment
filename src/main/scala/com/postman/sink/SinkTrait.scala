package com.postman.sink

import org.apache.spark.sql.DataFrame

import java.util.Properties

trait SinkTrait {
  def getSinkConnectionProperties: Properties
  def pushToSink(sourceDataFrame: DataFrame): Unit
}
