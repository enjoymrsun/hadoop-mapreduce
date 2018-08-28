package com.xiangshi.nio;

import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOClient {

  public static void main(String[] args) throws Exception {
    ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);

    // 挑选器
    Selector selector = Selector.open();

    // 套接字通道
    SocketChannel clientChannel = SocketChannel.open();
    clientChannel.connect(new InetSocketAddress("localhost", 9999));
    clientChannel.configureBlocking(false);
    clientChannel.register(selector, SelectionKey.OP_READ);

    new Thread() {
      @Override
      public void run() {
        ByteBuffer threadBuffer = ByteBuffer.allocate(1024 * 5);
        int index = 1;

        while (true) {

          try {
            byte[] bytes = ("from Client Abraham" + index++).getBytes();
            threadBuffer.put(bytes);
            threadBuffer.flip();
            clientChannel.write(threadBuffer);
            threadBuffer.clear();
            Thread.sleep(500);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
    }.start();

    while (true) {
      selector.select();

      Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
      while (keys.hasNext()) {
        SelectionKey k = keys.next();

        if (k.isReadable()) {
          SocketChannel sc = (SocketChannel) k.channel();

          ByteArrayOutputStream baos = new ByteArrayOutputStream();

          // 读取通道的数据，写入到baos中。
          while (sc.read(buffer) > 0) {
            buffer.flip();  // 拍板
            baos.write(buffer.array(), buffer.position(), buffer.limit());
            buffer.clear();
          }

          System.out.println(new String(baos.toByteArray()));
        }
      }

      selector.selectedKeys().clear();
    }
  }

}
