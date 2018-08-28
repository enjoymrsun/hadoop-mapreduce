package com.xiangshi.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;

/**
 */
public class SQLJava {
  public static void main(String[] args) {
    SparkConf conf = new SparkConf();
    conf.setMaster("local");
    conf.setAppName("SQLJava");
    SparkSession session = SparkSession.builder()
            .appName("SQLJava")
            .config("spark.master", "local")
            .getOrCreate();
    Dataset<Row> df1 = session.read().json("file:///d:/scala/json.dat");
    //创建临时视图
    df1.createOrReplaceTempView("customers");
    df1.show();

//        Dataset<Row> df3 = df1.where("age > 13");
//        df3.show();

    //按照sql方式查询
    Dataset<Row> df2 = session.sql("select * from customers where age > 13");
    df2.show();
    System.out.println("=================");

    //聚合查询
    Dataset<Row> dfCount = session.sql("select count(1) from customers");
    dfCount.show();

    //DataFrame和RDD互操作
//        JavaRDD<Row> rdd = df1.toJavaRDD();
//        rdd.collect().forEach(new Consumer<Row>() {
//            public void accept(Row row) {
//                long age = row.getLong(0);
//                long id = row.getLong(1);
//                String name = row.getString(2);
//                System.out.println(age + "," + id + "," + name);
//            }
//        });

    //保存处理,设置保存模式
    df2.write().mode(SaveMode.Append).json("file:///d:/scala/json/out.dat");

  }
}
