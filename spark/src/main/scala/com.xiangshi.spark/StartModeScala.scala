import java.io.File

import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object StartModeScala {
    var count = 3 ;
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        conf.setMaster("local[4,0]") ;

        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(1 to 20);
        val rdd2 = rdd1.map(e=>{
            val tname = Thread.currentThread().getName;
            println(tname + " : " + e)
            if(e == 2){
                if(count != 0){
                    count -= 1 ;
                    throw new Exception();
                }
                else{
                    e ;
                }
            }
            else{
                e
            }
        })
        println(rdd2.reduce(_ + _))
    }
}
