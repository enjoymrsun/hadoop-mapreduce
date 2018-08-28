package com.xiangshi.mr.multiinput;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 *
 */
public class WCApp {
  public static void main(String[] args) throws Exception {

    Configuration conf = new Configuration();

    conf.set("fs.defaultFS", "file:///");

    Job job = Job.getInstance(conf);

    //设置job的各种属性
    job.setJobName("WCAppMulti");                        //作业名称
    job.setJarByClass(WCApp.class);                 //搜索类

    //多个输入
    MultipleInputs.addInputPath(job, new Path("file:///d:/mr/txt"), TextInputFormat.class, WCTextMapper.class);
    MultipleInputs.addInputPath(job, new Path("file:///d:/mr/seq"), SequenceFileInputFormat.class, WCSeqMapper.class);

    //设置输出
    FileOutputFormat.setOutputPath(job, new Path(args[0]));

    job.setReducerClass(WCReducer.class);           //reducer类
    job.setNumReduceTasks(3);                       //reduce个数

    job.setOutputKeyClass(Text.class);
    job.setOutputValueClass(IntWritable.class);     //

    job.waitForCompletion(true);
  }
}
