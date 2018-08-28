package com.xiangshi.spark;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.streaming.Seconds;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaPairDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import scala.Tuple2;

/**
 */
public class KafkaSparkStreamingDemo {
  public static void main(String[] args) throws InterruptedException {

    SparkConf conf = new SparkConf();
    conf.setAppName("kafkaSpark");
    conf.setMaster("local[4]");
    //创建Spark流应用上下文
    JavaStreamingContext streamingContext = new JavaStreamingContext(conf, Seconds.apply(5));

    Map<String, Object> kafkaParams = new HashMap<>();
    kafkaParams.put("bootstrap.servers", "s202:9092,s203:9092");
    kafkaParams.put("key.deserializer", StringDeserializer.class);
    kafkaParams.put("value.deserializer", StringDeserializer.class);
    kafkaParams.put("group.id", "g6");
    kafkaParams.put("auto.offset.reset", "latest");
    kafkaParams.put("enable.auto.commit", false);

    Collection<String> topics = Arrays.asList("mytopic1");

    final JavaInputDStream<ConsumerRecord<String, String>> stream =
            KafkaUtils.createDirectStream(
                    streamingContext,
                    LocationStrategies.PreferConsistent(),
                    ConsumerStrategies.<String, String>Subscribe(topics, kafkaParams)
            );

    //压扁
    JavaDStream<String> wordsDS = stream.flatMap(new FlatMapFunction<ConsumerRecord<String, String>, String>() {
      public Iterator<String> call(ConsumerRecord<String, String> r) throws Exception {
        String value = r.value();
        List<String> list = new ArrayList<String>();
        String[] arr = value.split(" ");
        for (String s : arr) {
          list.add(s);
        }
        return list.iterator();
      }
    });

    //映射成元组
    JavaPairDStream<String, Integer> pairDS = wordsDS.mapToPair(new PairFunction<String, String, Integer>() {
      public Tuple2<String, Integer> call(String s) throws Exception {
        return new Tuple2<String, Integer>(s, 1);
      }
    });

    //聚合
    JavaPairDStream<String, Integer> countDS = pairDS.reduceByKey(new Function2<Integer, Integer, Integer>() {
      public Integer call(Integer v1, Integer v2) throws Exception {
        return v1 + v2;
      }
    });
    //打印
    countDS.print();

    streamingContext.start();

    streamingContext.awaitTermination();
  }
}
