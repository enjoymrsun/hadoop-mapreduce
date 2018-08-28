package com.xiangshi.mr.skew.stage2;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.Random;

/**
 * 自定义分区函数
 */
public class RandomPartitioner extends Partitioner<Text, IntWritable> {

  public int getPartition(Text text, IntWritable intWritable, int numPartitions) {
    return new Random().nextInt(numPartitions);
  }
}
