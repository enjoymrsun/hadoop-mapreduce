package com.xiangshi.mr.skew.stage2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * WCTextMapper
 */
public class WCSkewMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  public WCSkewMapper() {
    System.out.println("new MaxTempMapper()");
  }

  protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    String[] arr = value.toString().split(" ");

    Text keyOut = new Text();
    IntWritable valueOut = new IntWritable();

    for (String s : arr) {
      keyOut.set(s);
      valueOut.set(1);
      context.write(keyOut, valueOut);
    }
  }
}
