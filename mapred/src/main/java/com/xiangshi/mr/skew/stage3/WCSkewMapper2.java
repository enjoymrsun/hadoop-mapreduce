package com.xiangshi.mr.skew.stage3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * WCTextMapper
 */
public class WCSkewMapper2 extends Mapper<LongWritable, Text, Text, IntWritable> {

  public WCSkewMapper2() {
    System.out.println("new MaxTempMapper()");
  }

  protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] arr = value.toString().split("\t");
    context.write(new Text(arr[0]), new IntWritable(Integer.parseInt(arr[1])));
  }
}
