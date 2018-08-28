import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object GroupByKeyDemo1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        conf.setMaster("local[4]") ;
        val sc = new SparkContext(conf)
        val rdd1 = sc.textFile("d:/scala/stus.txt",4)
        val rdd2 = rdd1.map(line=>{
            val key = line.split(" ")(3)
            (key,line)
        })
        val rdd3 = rdd2.groupByKey()
        rdd3.collect().foreach(t=>{
            val key = t._1;
            println(key + " : ====================")
            for (e <- t._2){
                println(e)
            }
        })
    }
}
