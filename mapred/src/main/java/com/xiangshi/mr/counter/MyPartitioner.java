package com.xiangshi.mr.counter;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * 自定义分区
 */
public class MyPartitioner extends Partitioner<Text, IntWritable> {
  public MyPartitioner() {
    System.out.println("new MyPartitioner()");
  }

  public int getPartition(Text text, IntWritable intWritable, int numPartitions) {
    System.out.println("MyPartitioner.getPartition()");
    return 0;
  }
}
