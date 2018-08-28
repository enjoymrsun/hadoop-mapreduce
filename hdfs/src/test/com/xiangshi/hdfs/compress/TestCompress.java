package com.xiangshi.hdfs.compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.CompressionInputStream;
import org.apache.hadoop.io.compress.CompressionOutputStream;
import org.apache.hadoop.io.compress.DeflateCodec;
import org.apache.hadoop.io.compress.GzipCodec;
import org.apache.hadoop.io.compress.Lz4Codec;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.hadoop.util.ReflectionUtils;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 测试压缩
 */
public class TestCompress {

  @Test
  public void deflateCompress() throws Exception {
    Class[] zipClasses = {
            DeflateCodec.class,
            GzipCodec.class,
            BZip2Codec.class,
            Lz4Codec.class,
            SnappyCodec.class
    };

    for (Class c : zipClasses) {
      unzip(c);
    }
  }

  /**
   * 压缩测试
   */
  public void zip(Class codecClass) throws Exception {
    long start = System.currentTimeMillis();
    //实例化对象
    CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, new Configuration());
    //创建文件输出流,得到默认扩展名
    FileOutputStream fos = new FileOutputStream("d:/comp/b" + codec.getDefaultExtension());
    //得到压缩流
    CompressionOutputStream zipOut = codec.createOutputStream(fos);
    IOUtils.copyBytes(new FileInputStream("d:/comp/a.txt"), zipOut, 1024);
    zipOut.close();
    System.out.println(codecClass.getSimpleName() + " : " + (System.currentTimeMillis() - start));
  }

  /**
   * 压缩测试
   */
  public void unzip(Class codecClass) throws Exception {
    long start = System.currentTimeMillis();
    //实例化对象
    CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, new Configuration());
    //创建文件输出流,得到默认扩展名
    FileInputStream fis = new FileInputStream("d:/comp/b" + codec.getDefaultExtension());
    //得到压缩流
    CompressionInputStream zipIn = codec.createInputStream(fis);
    IOUtils.copyBytes(zipIn, new FileOutputStream("d:/comp/b" + codec.getDefaultExtension() + ".txt"), 1024);
    zipIn.close();
    System.out.println(codecClass.getSimpleName() + " : " + (System.currentTimeMillis() - start));
  }
}