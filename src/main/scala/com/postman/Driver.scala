package com.postman

import com.postman.reader.SourceReader
import com.postman.sink.SinkTrait
import com.postman.utils.CommonUtils.{getReaderFromSourceType, getSinkFromSinkType}
import com.postman.utils.{Constants, SparkUtils}
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.slf4j.LoggerFactory

object Driver {

  def main(args: Array[String]): Unit = {

    val logger = LoggerFactory.getLogger("Ingestion Logger")

    implicit val spark: SparkSession = SparkUtils.getSparkSession

    // hardcoding spark conf properties which should have been passed during runtime.
    spark.conf.set("spark.input.filePath", "src/main/resources/products.csv")
    spark.conf.set("spark.output.aggregatedTableName", "aggregated_tbl")
    spark.conf.set("spark.output.productTableName", "product")
    spark.conf.set("spark.jdbc.username", "test")
    spark.conf.set("spark.jdbc.password", "test")
    spark.conf.set("spark.jdbc.jdbcUrl", "jdbc:postgresql://localhost:5432/test")
    spark.conf.set("spark.jdbc.driver", "org.postgresql.Driver")
    spark.conf.set("spark.input.sinkType", "jdbc")

    ingestDataFromFile
    ingestAggregatedData

    logger.info("********Records are ingested successfully*********")
  }

  def ingestDataFromFile(implicit spark: SparkSession): Unit = {
    val sourceReader: SourceReader = getReaderFromSourceType(Constants.Csv)

    val filePath = spark.conf.get("spark.input.filePath")
    val sourceDataFrame = sourceReader.getDataFrameFromFile(filePath)

    val sink: SinkTrait = getSinkFromSinkType
    sink.pushToSink(sourceDataFrame)

  }

  def ingestAggregatedData(implicit spark: SparkSession): Unit = {
    val sourceReader: SourceReader = getReaderFromSourceType(Constants.Jdbc)
    val aggregatedTableName: String = spark.conf.get("spark.output.aggregatedTableName")
    val productTableName: String = spark.conf.get("spark.output.productTableName")

    val connProps = SparkUtils.getJdbcConnectionProps
    val jdbcUrl = connProps.getProperty("jdbcUrl")

    val productDF = sourceReader.getDataFrameFromJdbc(connProps, productTableName)

    val aggregatedDF = productDF.groupBy("name").count().select(col("name"), col("count").as("no_of_products"))

    aggregatedDF.write.mode(SaveMode.Overwrite).jdbc(jdbcUrl, aggregatedTableName, connProps)
  }

}
