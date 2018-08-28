package com.xiangshi.nio;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

public class NIOServer {
  public static void main(String[] args) throws Exception {
    ByteBuffer buffer = ByteBuffer.allocate(1024);

    // 开启挑选器
    Selector selector = Selector.open();

    // 开启ServerSocket通道
    ServerSocketChannel ssc = ServerSocketChannel.open();
    // 绑定端口
    ssc.bind(new InetSocketAddress("localhost", 9999));
    // 将ServerChannel设置成非阻塞的
    ssc.configureBlocking(false);
    // 向Selector里注册这个服务端channel，获得对应的SelectionKey
    ssc.register(selector, SelectionKey.OP_ACCEPT);


    while (true) {
      // 此方法是阻塞的
      selector.select();
      // 迭代挑选出来的key集合
      Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
      while (keys.hasNext()) {
        SelectionKey k = keys.next();

        // 是可接受的事情
        if (k.isAcceptable()) {
          // 得到 感兴趣的事发生了的channel
          ServerSocketChannel channel0 = (ServerSocketChannel) k.channel();
          SocketChannel sc = channel0.accept();
          // 设置SocketChannel为非阻塞
          sc.configureBlocking(false);

          // 注册读事件
          sc.register(selector, SelectionKey.OP_READ);
        }

        // 可读
        if (k.isReadable()) {
          SocketChannel sc = (SocketChannel) k.channel();

          // 加一个服务器转发的前缀
          byte[] bytes = ("Transfer from Server: ").getBytes();
          buffer.put(bytes, 0, bytes.length);

          while (sc.read(buffer) > 0) {
            buffer.flip();  // 拍板
            sc.write(buffer);
            buffer.clear();
          }
        }
      }

      // 清除所有挑选出来的key
      selector.selectedKeys().clear();
    }
  }
}
