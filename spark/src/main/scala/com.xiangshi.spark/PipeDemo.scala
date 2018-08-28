import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object PipeDemo1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        conf.setMaster("local[4]") ;

        val sc = new SparkContext(conf)
        val rdd = sc.parallelize(Array("file:///d:","file:///e:","file:///f:",3))
        val rdd0 = rdd.pipe("ls ")
        rdd0.collect().foreach(println)
    }
}
