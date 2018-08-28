package com.xiangshi.mr.maxtemp.normal;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * WCTextMapper
 */
public class MaxTempMapper extends Mapper<LongWritable, Text, IntWritable, IntWritable> {
  protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    String line = value.toString();
    String arr[] = line.split(" ");
    context.write(new IntWritable(Integer.parseInt(arr[0])), new IntWritable(Integer.parseInt(arr[1])));
  }
}
