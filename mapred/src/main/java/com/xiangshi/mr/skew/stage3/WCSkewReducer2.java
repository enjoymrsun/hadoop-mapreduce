package com.xiangshi.mr.skew.stage3;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Reducer
 */
public class WCSkewReducer2 extends Reducer<Text, IntWritable, Text, IntWritable> {
  /**
   * reduce
   */
  protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
    int count = 0;
    for (IntWritable iw : values) {
      count = count + iw.get();
    }
    context.write(key, new IntWritable(count));
  }
}
