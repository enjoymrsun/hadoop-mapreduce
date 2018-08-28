package com.xiangshi.network.tcp.qq.server;

public class QQServerMain {

  public static void main(String[] args) {
    System.out.println("服务器启动了....");
    QQServer.getInstance().start();
  }
}
