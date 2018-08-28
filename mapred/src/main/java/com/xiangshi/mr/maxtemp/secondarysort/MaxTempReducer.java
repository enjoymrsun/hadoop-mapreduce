package com.xiangshi.mr.maxtemp.secondarysort;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/**
 * Reducer
 */
public class MaxTempReducer extends Reducer<ComboKey, NullWritable, IntWritable, IntWritable> {

  /**
   */
  protected void reduce(ComboKey key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
    System.out.println("MaxTempReducer.reduce : key = " + key);
    int year = key.getYear();
    int temp = key.getTemp();
    for (NullWritable v : values) {
      System.out.println(key.getYear() + " : " + key.getTemp());
    }
    context.write(new IntWritable(year), new IntWritable(temp));
  }
}
