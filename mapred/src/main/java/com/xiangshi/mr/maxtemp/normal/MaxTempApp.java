package com.xiangshi.mr.maxtemp.normal;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 */
public class MaxTempApp {
  public static void main(String[] args) throws Exception {

    Configuration conf = new Configuration();
    conf.set("fs.defaultFS", "file:///");

    Job job = Job.getInstance(conf);

    //设置job的各种属性
    job.setJobName("MaxTempApp");                        //作业名称
    job.setJarByClass(MaxTempApp.class);                 //搜索类
    job.setInputFormatClass(TextInputFormat.class); //设置输入格式

    //添加输入路径
    FileInputFormat.addInputPath(job, new Path(args[0]));
    //设置输出路径
    FileOutputFormat.setOutputPath(job, new Path(args[1]));

    job.setPartitionerClass(YearPartitioner.class);

    job.setMapperClass(MaxTempMapper.class);             //mapper类
    job.setReducerClass(MaxTempReducer.class);           //reducer类

    job.setNumReduceTasks(3);                           //reduce个数

    job.setMapOutputKeyClass(IntWritable.class);        //
    job.setMapOutputValueClass(IntWritable.class);      //

    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);         //
    job.waitForCompletion(true);
  }
}