package com.postman.sink

import com.postman.utils.{Constants, SparkUtils}
import org.apache.spark.sql.{DataFrame, SparkSession}

import java.sql.{Connection, DriverManager, PreparedStatement}
import java.util.Properties

case class JdbcSink(implicit spark: SparkSession) extends SinkTrait {

  override def getSinkConnectionProperties: Properties = SparkUtils.getJdbcConnectionProps

  override def pushToSink(sourceDataFrame: DataFrame): Unit = {
    val broadcastConnectionProps = spark.sparkContext.broadcast(getSinkConnectionProperties)

    sourceDataFrame.coalesce(50).foreachPartition( partition => {

      val connProps = broadcastConnectionProps.value
      val jdbcUrl = connProps.getProperty(Constants.JdbcUrl)
      val username = connProps.getProperty(Constants.Username)
      val password = connProps.getProperty(Constants.Password)
      val driver = connProps.getProperty(Constants.Driver)

      Class.forName(driver)

      val conn: Connection = DriverManager.getConnection(jdbcUrl, username, password)
      var stmt: PreparedStatement = null

      partition.grouped(Constants.DbBatchSize).foreach(batch => {

        batch.foreach(row => {
          {
            val name = row.getAs[String]("name")
            val sku = row.getAs[String]("sku")
            val description = row.getAs[String]("description")

            stmt = conn.prepareStatement(Constants.UpsertStmt.format(name, sku, description, name, sku, description))
            stmt.addBatch()
          }
          stmt.executeBatch()
        })

      })

      conn.close()
    })
  }
}
