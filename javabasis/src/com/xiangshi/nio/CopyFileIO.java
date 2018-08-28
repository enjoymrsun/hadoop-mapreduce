package com.xiangshi.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 普通的复制文件 利用FileInputStream 和FileOutputStream
 */
public class CopyFileIO {
  public static void main(String[] args) throws Exception {
    long start = System.currentTimeMillis();
    FileInputStream fis = new FileInputStream("data/video.mov");

    FileOutputStream fos = new FileOutputStream("data/io.mov");

    byte[] buffer = new byte[8 * 1024];
    int len = -1;
    while ((len = fis.read(buffer)) != -1) {
      fos.write(buffer, 0, len);
    }

    fos.close();
    fis.close();

    System.out.println("IO: " + (System.currentTimeMillis() - start));
  }
}
