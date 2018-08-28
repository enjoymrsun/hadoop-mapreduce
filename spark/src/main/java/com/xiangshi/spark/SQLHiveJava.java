package com.xiangshi.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * 2018/4/3.
 */
public class SQLHiveJava {
  public static void main(String[] args) {
    SparkConf conf = new SparkConf();
    conf.setMaster("local");
    conf.setAppName("SQLJava");
    SparkSession sess = SparkSession.builder()
            .appName("HiveSQLJava")
            .config("spark.master", "local")
            .getOrCreate();
    Dataset<Row> df = sess.sql("create table tttt(id int,name string,age int)");
    df.show();

  }
}
