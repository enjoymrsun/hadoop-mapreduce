import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object UnionDemo1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        conf.setMaster("local[4]") ;
        val sc = new SparkContext(conf)
        val rdd1 = sc.textFile("d:/scala/log.txt",4)
        //所有error
        val errorRDD = rdd1.filter(_.toLowerCase.contains("error"))

        //所有warn行
        val warnRDD = rdd1.filter(_.toLowerCase.contains("warn"));

        val allRDD = errorRDD.union(warnRDD);

        allRDD.collect().foreach(println)



    }
}
