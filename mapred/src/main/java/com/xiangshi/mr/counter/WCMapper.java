package com.xiangshi.mr.counter;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * WCTextMapper
 */
public class WCMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

  public WCMapper() {
    System.out.println("new MaxTempMapper()");
  }


  protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
    Text keyOut = new Text();
    IntWritable valueOut = new IntWritable();
    String[] arr = value.toString().split(" ");
    for (String s : arr) {
      keyOut.set(s);
      valueOut.set(1);
      context.write(keyOut, valueOut);
      context.getCounter("m", Util.getInfo(this, "map")).increment(1);
    }
  }
}
