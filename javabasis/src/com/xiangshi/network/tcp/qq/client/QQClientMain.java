package com.xiangshi.network.tcp.qq.client;

import java.net.Socket;

public class QQClientMain {
  public static void main(String[] args) throws Exception {
    QQClientUI ui = new QQClientUI();
    Socket sock = new Socket("192.168.1.7", 8888);
    String localIP = sock.getLocalAddress().getHostAddress();
    int port = sock.getLocalPort();
    ui.setTitle(localIP + ":" + port);
    ClientCommThread thread = new ClientCommThread(sock, ui);
    thread.start();
    ui.setCommThread(thread);
  }

}
