package com.xiangshi.network.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class UDPSender {
  public static void main(String[] args) throws Exception {
    SocketAddress receiverAddress = new InetSocketAddress("localhost", 8888);
    DatagramSocket sendSocket = new DatagramSocket(9999);

    int index = 1;
    for (; ; ) {
      // 构造数据缓冲区 形成数据包
      byte[] data = String.format("fighting-%03d", index++).getBytes();
      DatagramPacket dataPacket = new DatagramPacket(data, data.length);
      // 应该在包上加上目的地址的IP和端口 所以之前写的有问题 没发发送数据
      dataPacket.setSocketAddress(receiverAddress);
      sendSocket.send(dataPacket);
      Thread.sleep(500);
    }
  }
}
