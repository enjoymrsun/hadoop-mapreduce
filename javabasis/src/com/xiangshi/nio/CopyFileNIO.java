package com.xiangshi.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class CopyFileNIO {

  public static void main(String[] args) throws Exception {
    long start = System.currentTimeMillis();
    FileInputStream fis = new FileInputStream("data/video.mov");
    FileChannel fcIn = fis.getChannel();

    FileOutputStream fos = new FileOutputStream("data/nio.mov");
    FileChannel fcOut = fos.getChannel();


    ByteBuffer buffer = ByteBuffer.allocate(8 * 1024);
    while (fcIn.read(buffer) != -1) {
      buffer.flip();
      fcOut.write(buffer);
      buffer.clear();
    }

    fcOut.close();
    fcIn.close();
    fos.close();
    fis.close();

    System.out.println("NIO: " + (System.currentTimeMillis() - start));
  }
}
