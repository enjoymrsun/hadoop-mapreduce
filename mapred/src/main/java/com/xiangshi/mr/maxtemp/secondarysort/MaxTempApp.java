package com.xiangshi.mr.maxtemp.secondarysort;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
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
    job.setJobName("SecondarySortApp");                        //作业名称
    job.setJarByClass(MaxTempApp.class);                 //搜索类
    job.setInputFormatClass(TextInputFormat.class); //设置输入格式

    //添加输入路径
    FileInputFormat.addInputPath(job, new Path("D:\\mr\\maxtemp\\temp.txt"));
    //设置输出路径
    FileOutputFormat.setOutputPath(job, new Path("D:\\mr\\maxtemp\\out"));

    job.setMapperClass(MaxTempMapper.class);             //mapper类
    job.setReducerClass(MaxTempReducer.class);           //reducer类

    //设置Map输出类型
    job.setMapOutputKeyClass(ComboKey.class);            //
    job.setMapOutputValueClass(NullWritable.class);      //

    //设置ReduceOutput类型
    job.setOutputKeyClass(IntWritable.class);
    job.setOutputValueClass(IntWritable.class);         //

    //设置分区类
    job.setPartitionerClass(YearPartitioner.class);
    //设置分组对比器
    job.setGroupingComparatorClass(YearGroupComparator.class);
    //设置排序对比器
    job.setSortComparatorClass(ComboKeyComparator.class);

    job.setNumReduceTasks(3);                           //reduce个数
    //
    job.waitForCompletion(true);
  }
}