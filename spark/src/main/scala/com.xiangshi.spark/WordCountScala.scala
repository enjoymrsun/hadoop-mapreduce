package com.it18zhang.spark.scala ;
import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object WordCountScala {
    def main(args: Array[String]): Unit = {
        //创建Spark配置对象
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        //设置master属性
        conf.setMaster("local[*]") ;

        //通过conf创建sc
        val sc = new SparkContext(conf)

        //加载文本文件
        val rdd1 = sc.textFile("d:/scala/test.txt")

        //flatMap压扁
        val rdd2 = rdd1.flatMap(line=>{
            println("flatMap : " + line)
            line.split(" ")
        }) ;

        //map变换
        val rdd3 = rdd2.map(word=>{
            println("map : " + word)
            (word,2)
        })
        val rdd4 = rdd3.reduceByKey((a,b)=>{
            println("reduceByKey : " + a + " , " + b)
            a + b
        });

        sc.setCheckpointDir("file:///d:/scala/check")
        rdd4.checkpoint()
        val f = rdd4.getCheckpointFile
        rdd4.collect()
        println(f)
    }
}
