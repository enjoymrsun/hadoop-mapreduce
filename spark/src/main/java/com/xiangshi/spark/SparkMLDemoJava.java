package com.xiangshi.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.ml.linalg.Vector;
import org.apache.spark.ml.linalg.Vectors;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.StructType;

/**
 * java版实现机器学习
 */
public class SparkMLDemoJava {
  public static void main(String[] args) {
    SparkConf conf = new SparkConf();
    conf.setAppName("ml");
    conf.setMaster("local[4]");
    JavaSparkContext jsc = new JavaSparkContext(conf);
    //创建RDD
    SparkSession sess = SparkSession.builder().config(conf).getOrCreate();
    JavaRDD<String> rdd1 = jsc.textFile("file:///D:/downloads/bigdata/ml/winequality-white.csv");

    //直接转换成RDD<Row>
    JavaRDD<Row> rdd2 = rdd1.map(new Function<String, Row>() {
      public Row call(String v1) throws Exception {
        String[] arr = v1.split(";");
        double label = Double.parseDouble(arr[11]);
        Vector v = Vectors.dense(Double.parseDouble(arr[0]),
                Double.parseDouble(arr[1]),
                Double.parseDouble(arr[2]),
                Double.parseDouble(arr[3]),
                Double.parseDouble(arr[4]),
                Double.parseDouble(arr[5]),
                Double.parseDouble(arr[6]),
                Double.parseDouble(arr[7]),
                Double.parseDouble(arr[8]),
                Double.parseDouble(arr[9]),
                Double.parseDouble(arr[10])
        );

        Row row = RowFactory.create(label, v);
        return row;
      }
    });
    StructType st = new StructType();

  }
}
