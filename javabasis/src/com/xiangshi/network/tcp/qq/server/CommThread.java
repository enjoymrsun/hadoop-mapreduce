package com.xiangshi.network.tcp.qq.server;


import com.xiangshi.network.tcp.qq.common.MessageFactory;
import com.xiangshi.network.tcp.qq.common.Util;

import java.net.Socket;


/**
 * 通信线程
 */
public class CommThread extends Thread {

  //socket
  private Socket sock;

  public CommThread(Socket sock) {
    this.sock = sock;
  }

  public void run() {
    while (true) {
      //解析client发来的消息
      try {
        MessageFactory.parseClientMessageAndSend(sock);
      } catch (Exception e) {
        String userInfo = Util.getUserInfoStr(sock);
        QQServer.getInstance().removeUser(userInfo);
        QQServer.getInstance().broadcastFriends();
      }
    }
  }
}