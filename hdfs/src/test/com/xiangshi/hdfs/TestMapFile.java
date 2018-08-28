package com.xiangshi.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.MapFile;
import org.apache.hadoop.io.Text;
import org.junit.Test;

/**
 * 序列文件
 */
public class TestMapFile {
  /**
   * 写操作
   */
  @Test
  public void save() throws Exception {
    Configuration conf = new Configuration();
    conf.set("fs.defaultFS", "file:///");
    FileSystem fs = FileSystem.get(conf);
    Path p = new Path("d:/seq/1.seq");
    MapFile.Writer writer = new MapFile.Writer(conf, fs, "d:/map", IntWritable.class, Text.class);
    for (int i = 0; i < 100; i++) {
      writer.append(new IntWritable(i), new Text("tom" + i));
      writer.append(new IntWritable(i), new Text("tom" + i));
    }
    writer.close();
  }

  /**
   * 操纵同步点
   */
  @Test
  public void read4() throws Exception {
    Configuration conf = new Configuration();
    conf.set("fs.defaultFS", "file:///");
    FileSystem fs = FileSystem.get(conf);
    Path p = new Path("d:/seq/1.seq");
    MapFile.Reader reader = new MapFile.Reader(fs, "d:/map", conf);
    IntWritable key = new IntWritable();
    Text value = new Text();
    while (reader.next(key, value)) {
      System.out.println(key.get() + ":" + value.toString());
    }
    reader.close();
  }
}