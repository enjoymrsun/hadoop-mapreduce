import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object CogoupDemo1 {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        conf.setMaster("local[4]") ;
        val sc = new SparkContext(conf)
        val rdd1 = sc.textFile("d:/scala/cogroup-1.txt",4)
        //K,V
        val rdd2 = rdd1.map(line=>{
            val arr = line.split(" ")
            (arr(0),arr(1))
        })

        //K,W
        val rdd3 = sc.textFile("d:/scala/cogroup-2.txt", 4)
        //key,value
        val rdd4 = rdd3.map(line => {
            val arr = line.split(" ")
            (arr(0), arr(1))
        })

        val rdd = rdd2.cogroup(rdd4)
        rdd.collect().foreach(t=>{
            println(t._1 + ":=================")
            for( e <- t._2._1){
                println(e)
            }
            for (e <- t._2._2) {
                println(e)
            }
        })
    }
}
