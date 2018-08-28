import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object FirstDemo1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        conf.setMaster("local[8]") ;
        val sc = new SparkContext(conf)

        val rdd1 = sc.textFile("d:/scala/test.txt",4)
        //println(rdd1.first())
        rdd1.take(3).foreach(println)
    }
}
