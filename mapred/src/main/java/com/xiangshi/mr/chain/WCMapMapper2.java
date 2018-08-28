package com.xiangshi.mr.chain;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 *
 */
public class WCMapMapper2 extends Mapper<Text, IntWritable, Text, IntWritable> {


  protected void map(Text key, IntWritable value, Context context) throws IOException, InterruptedException {
    if (!key.toString().equals("falungong")) {
      context.write(key, value);
    }
  }
}
