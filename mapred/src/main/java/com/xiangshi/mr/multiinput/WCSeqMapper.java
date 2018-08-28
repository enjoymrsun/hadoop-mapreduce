package com.xiangshi.mr.multiinput;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * WCTextMapper
 */
public class WCSeqMapper extends Mapper<IntWritable, Text, Text, IntWritable> {
  protected void map(IntWritable key, Text value, Context context) throws IOException, InterruptedException {
    Text keyOut = new Text();
    IntWritable valueOut = new IntWritable();
    String[] arr = value.toString().split(" ");
    for (String s : arr) {
      keyOut.set(s);
      valueOut.set(1);
      context.write(keyOut, valueOut);
    }
  }
}
