import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object JoinDemo1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        conf.setMaster("local[4]") ;
        //名单
        val sc = new SparkContext(conf)
        val namesRDD1 = sc.textFile("d:/scala/names.txt");
        val namesRDD2 = namesRDD1.map(line=>{
            var arr = line.split(" ")
            (arr(0).toInt,arr(1))
        })
        //总成绩
        val scoreRDD1 = sc.textFile("d:/scala/scores.txt");
        val scoreRDD2 = scoreRDD1.map(line => {
            var arr = line.split(" ")
            (arr(0).toInt, arr(1).toInt)
        })

        val rdd = namesRDD2.join(scoreRDD2)
        rdd.collect().foreach(t=>{
            println(t._1 + " : " + t._2)
        })
    }
}
