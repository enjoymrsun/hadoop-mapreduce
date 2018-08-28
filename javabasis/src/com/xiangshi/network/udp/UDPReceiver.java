package com.xiangshi.network.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UDPReceiver {
  public static void main(String[] args) throws Exception {
    DatagramSocket receiveSocket = new DatagramSocket(8888);

    byte[] bytes = new byte[1024];
    DatagramPacket dataPacket = new DatagramPacket(bytes, bytes.length);

    while (true) {
      receiveSocket.receive(dataPacket);

      String message = new String(dataPacket.getData(), 0, dataPacket.getLength());
      System.out.println("receiver receive: " + message);
    }

  }
}
