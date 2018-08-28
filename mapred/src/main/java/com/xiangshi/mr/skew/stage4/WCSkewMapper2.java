package com.xiangshi.mr.skew.stage4;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * WCTextMapper
 */
public class WCSkewMapper2 extends Mapper<Text, Text, Text, IntWritable> {

  protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
    context.write(key, new IntWritable(Integer.parseInt(value.toString())));
  }
}
