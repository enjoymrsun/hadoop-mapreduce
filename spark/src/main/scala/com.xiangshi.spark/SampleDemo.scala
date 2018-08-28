import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object SampleDemo1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        conf.setMaster("local[4]") ;
        val sc = new SparkContext(conf)
        val rdd1 = sc.textFile("d:/scala/test.txt",4)
        val rdd2 = rdd1.flatMap(_.split(" "))

        val rdd3 = rdd2.sample(false,0.5)
        rdd3.collect().foreach(println)

    }
}
