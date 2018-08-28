package com.xiangshi.mr.mysql;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;

/**
 *
 */
public class WCApp {
  public static void main(String[] args) throws Exception {

    Configuration conf = new Configuration();
    Job job = Job.getInstance(conf);

    //设置job的各种属性
    job.setJobName("MySQLApp");                        //作业名称
    job.setJarByClass(WCApp.class);                 //搜索类

    //配置数据库信息
    String driverclass = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://192.168.231.1:3306/big4";
    String username = "root";
    String password = "root";
    //设置数据库配置
    DBConfiguration.configureDB(job.getConfiguration(), driverclass, url, username, password);
    //设置数据输入内容
    DBInputFormat.setInput(job, MyDBWritable.class, "select id,name,txt from words", "select count(*) from words");

    //
    DBOutputFormat.setOutput(job, "stats", "word", "c");

    //设置分区类
    job.setMapperClass(WCMapper.class);             //mapper类
    job.setReducerClass(WCReducer.class);           //reducer类

    job.setNumReduceTasks(3);                       //reduce个数

    job.setMapOutputKeyClass(Text.class);           //
    job.setMapOutputValueClass(IntWritable.class);  //

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);     //

    job.waitForCompletion(true);
  }
}