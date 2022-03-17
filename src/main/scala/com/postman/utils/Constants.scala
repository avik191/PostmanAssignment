package com.postman.utils

object Constants {

  val Csv = "csv"
  val Jdbc = "jdbc"
  val JdbcUrl = "jdbcUrl"
  val Username = "user"
  val Password = "password"
  val Driver = "driver"
  val DbBatchSize = 500
  val UpsertStmt = "INSERT INTO product (name, sku, description) VALUES ('%s','%s', '%s') ON CONFLICT (sku) DO UPDATE SET name = '%s', sku = '%s', description = '%s';"

}
