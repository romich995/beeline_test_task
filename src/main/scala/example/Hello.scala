package example
import org.apache.spark
import org.apache.spark.sql.{DataFrame, DataFrameReader, Column}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.types.{StructField, StructType, StringType, LongType, DateType, DoubleType}
import org.apache.spark.sql.types.Metadata
import org.apache.spark.sql.functions.{rank, col}
import org.apache.spark.sql.SparkSession



object Hello extends App {

case class Arguments(orders: String = "",
                      customers: String = "",
                      products: String = "",
                      popularProducts: String = "")

val parser = new scopt.OptionParser[Arguments]("parser") {
  head("scopt", "4.x")

  opt[String]('o', "orders")
    .required()
    .valueName("<file>")
    .action( (x,c) => c.copy(orders = x) )
    .text("orders is a required file property")


  opt[String]('c', "customers")
    .required()
    .valueName("<file>")
    .action( (x,c) => c.copy(customers = x) )
    .text("customers is a required file property")

  opt[String]('p', "products")
    .required()
    .valueName("<file>")
    .action( (x,c) => c.copy(products = x) )
    .text("products is a required file property")


  opt[String]('r',"popularProducts")
    .required()
    .valueName("<file>")
    .action( (x,c) => c.copy(popularProducts = x) )
    .text("popularProducts is a required file property")

}

parser.parse(args, Arguments()) match {
case Some(arguments) => run(arguments)
case None => 

}


def run(arguments: Arguments): Unit = {
  val spark = SparkSession.builder
      .master("local")
      .appName("PopularProducts")
      .getOrCreate()
  


val CustomerSchema = StructType(Array(
  StructField("id", LongType, false),
  StructField("name", StringType, true),
  StructField("email", StringType, true),
  StructField("joinDate",  DateType, true),
  StructField("status", StringType, true)
))

val ProductSchema = StructType(Array(
  StructField("id", LongType, false),
  StructField("name", StringType, true),
  StructField("price", DoubleType, true),
  StructField("numberOfProducts",  LongType, true)
))

val OrderSchema = StructType(Array(
  StructField("customerID", LongType, false),
  StructField("orderID", LongType, false),
  StructField("productID", LongType, false),
  StructField("numberOfProduct",  LongType, true),
  StructField("orderDate", DateType, true),
  StructField("status", StringType, true)
))


def read_tsv(fp: String, schema: StructType): DataFrame = {
  return spark.read
      .format("csv")
      .option("delimiter", "\t")
      .option("header", false)
      .schema(schema)
      .load(fp)
}

val orders = read_tsv(arguments.orders, OrderSchema)

val customers = read_tsv(arguments.customers, CustomerSchema)

val products = read_tsv(arguments.products, ProductSchema)

val dfProductCustomer = orders
  .where(col("status") === "delivered")
  .groupBy(col("customerID"), col("productID"))
  .sum("numberOfProduct")
  .withColumnRenamed("sum(numberOfProduct)", "countOfProducts")

val window =  Window
  .partitionBy("customerID")
  .orderBy(col("countOfProducts").desc)

val rankByQuantity = rank().over(window)

val dfMostPopularPairs = dfProductCustomer.select(
  col("customerID"),
  col("productID"),
  rankByQuantity.alias("rankByQuantity"))
.where(col("rankByQuantity") === 1)

val joinType = "left_outer"
val joinExpessionMostPopular = customers.col("id") === dfMostPopularPairs.col("customerID")
val joinExpessionProduct = dfMostPopularPairs.col("productID") === products.col("id")

val resultDataFrame = customers
.join(dfMostPopularPairs, joinExpessionMostPopular, joinType)
.join(products, joinExpessionProduct, joinType)
.select(customers.col("name").alias("customerName"), products.col("name").alias("productName"))

resultDataFrame.repartition(1).write.format("csv")
.option("mode", "OVERWRITE")
.option("header", true)
.option("dateFormat", "yyyy-MM-dd")
.option("path", arguments.popularProducts)
.save()
}
}

