package com.xiangshi.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.Function0;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * 2018/4/3.
 */
public class JavaSparkStreamingWordCountWindowsApp {
  static JavaReceiverInputDStream sock;

  public static void main(String[] args) throws Exception {
    Function0<JavaStreamingContext> contextFactory = new Function0<JavaStreamingContext>() {
      public JavaStreamingContext call() {
        SparkConf conf = new SparkConf();
        conf.setMaster("local[4]");
        conf.setAppName("wc");
        JavaStreamingContext jssc = new JavaStreamingContext(conf, new Duration(2000));
        JavaDStream<String> lines = jssc.socketTextStream("localhost", 9999);
        JavaDStream<Long> dsCount = lines.countByWindow(new Duration(24 * 60 * 60 * 1000), new Duration(2000));
        dsCount.print();
        jssc.checkpoint("file:///d:/scala/check");
        return jssc;
      }
    };

    JavaStreamingContext context = JavaStreamingContext.getOrCreate("file:///d:/scala/check", contextFactory);

    context.start();
    context.awaitTermination();
  }
}
