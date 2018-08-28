import java.net.{InetAddress, Socket}

import org.apache.spark.storage.StorageLevel
import org.apache.spark.{SparkConf, SparkContext}

/**
  *
  */
object PersistScala {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        conf.setAppName("WordCountScala")
        conf.setMaster("local[4,3]") ;

        val sc = new SparkContext(conf)
        val rdd1 = sc.parallelize(1 to 20);
        val rdd2 = rdd1.map(e=>{

            println(e) ;
            e * 2
        })
        rdd2.persist(StorageLevel.DISK_ONLY)
        println(rdd2.reduce(_ + _))
        rdd2.unpersist();
        println(rdd2.reduce(_ + _))


        var str:String = java.net.InetAddress.getLocalHost.getHostAddress ;
        str = str + " : " + Thread.currentThread().getName + "\r\n";

        val sock  = new java.net.Socket("s201",8888);
        val out = sock.getOutputStream;
        out.write(str.getBytes())
        out.flush()
        out.close();
        sock.close();

        sc.broadcast()

        println(java.net.InetAddress.getLocalHost.getHostAddress + " : " + Thread.currentThread().getName + "")
        rdd1.collect()
    }
}