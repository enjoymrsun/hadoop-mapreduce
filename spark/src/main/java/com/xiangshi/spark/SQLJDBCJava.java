package com.xiangshi.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.sql.Column;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Properties;

/**
 * 2018/4/3.
 */
public class SQLJDBCJava {
  public static void main(String[] args) {
    SparkConf conf = new SparkConf();
    conf.setMaster("local");
    conf.setAppName("SQLJava");
    SparkSession sess = SparkSession.builder()
            .appName("SQLJava")
            .config("spark.master", "local")
            .getOrCreate();
    String url = "jdbc:mysql://192.168.231.1:3306/mybatis";
    String table = "persons";

    //查询数据库
    Dataset<Row> df = sess.read()
            .format("jdbc")
            .option("url", url)
            .option("dbtable", table)
            .option("user", "root")
            .option("password", "root")
            .option("driver", "com.mysql.jdbc.Driver")
            .load();
    df.show();

    //投影查询
    Dataset<Row> df2 = df.select(new Column("name"), new Column("phone"));
    //过滤
    df2 = df2.where("phone like '135%'");
    //去重
    df2 = df2.distinct();


    Properties prop = new Properties();
    prop.put("user", "root");
    prop.put("password", "root");
    prop.put("driver", "com.mysql.jdbc.Driver");

    //写入
    df2.write().jdbc(url, "subpersons", prop);
    df2.show();

  }
}
