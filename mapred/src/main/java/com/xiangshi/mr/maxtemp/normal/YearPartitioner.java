package com.xiangshi.mr.maxtemp.normal;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 2018/3/14.
 */
public class YearPartitioner extends Partitioner<IntWritable, IntWritable> {

  //3
  public int getPartition(IntWritable year, IntWritable temp, int parts) {
    int y = year.get() - 1970;
    if (y < 33) {
      return 0;
    } else if (y >= 33 && y < 66) {
      return 1;
    } else {
      return 2;
    }
  }
}
