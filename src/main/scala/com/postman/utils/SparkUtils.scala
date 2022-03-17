package com.postman.utils

import org.apache.spark.sql.SparkSession

import java.util.Properties

object SparkUtils {

  def getSparkSession: SparkSession = {
    SparkSession.builder()
      .appName("Product Ingestion Flow")
      .master("local[*]")
      .getOrCreate()
  }

  def getJdbcConnectionProps(implicit spark: SparkSession): Properties = {

    val username = spark.conf.get("spark.jdbc.username")
    val password = spark.conf.get("spark.jdbc.password")
    val jdbcUrl = spark.conf.get("spark.jdbc.jdbcUrl")
    val driver = spark.conf.get("spark.jdbc.driver")

    val connectionProperties: Properties = new Properties()
    connectionProperties.put("user", username)
    connectionProperties.put("password", password)
    connectionProperties.put("jdbcUrl", jdbcUrl)
    connectionProperties.put("driver", driver)

    connectionProperties
  }


}
