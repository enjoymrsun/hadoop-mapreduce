package com.xiangshi.network.tcp.qq.client;

import com.xiangshi.network.tcp.qq.common.Message;
import com.xiangshi.network.tcp.qq.common.MessageFactory;
import com.xiangshi.network.tcp.qq.common.ServerChatsMessage;
import com.xiangshi.network.tcp.qq.common.ServerRefreshFriendsMessage;
import com.xiangshi.network.tcp.qq.common.ServerSingleChatMessage;
import com.xiangshi.network.tcp.qq.common.Util;

import java.io.IOException;
import java.net.Socket;
import java.util.List;


/**
 * 接受消息线程
 */
public class ClientCommThread extends Thread {

  private Socket sock;

  private QQClientUI ui;

  public ClientCommThread(Socket sock, QQClientUI ui) {
    this.sock = sock;
    this.ui = ui;
    this.setDaemon(true);
  }

  public void run() {
    while (true) {
      try {
        Message msg = MessageFactory.parseServerMessage(sock);
        //群聊
        if (msg.getClass() == ServerChatsMessage.class) {
          ServerChatsMessage m = (ServerChatsMessage) msg;
          String senderInfo = new String(m.getSenderInfoBytes());
          String msgStr = new String(m.getMsgBytes());
          //添加消息到历史区
          ui.addMsgToHistory(senderInfo, msgStr);
        }
        //私聊
        else if (msg.getClass() == ServerSingleChatMessage.class) {
          ServerSingleChatMessage m = (ServerSingleChatMessage) msg;
          String senderInfo = new String(m.getSenderInfoBytes());
          String msgStr = new String(m.getMessage());

          //TODO 私聊
          QQClientChatSingleUI sui = null;
          if (!ui.windows.containsKey(senderInfo)) {
            sui = new QQClientChatSingleUI(this, senderInfo);
            ui.windows.put(senderInfo, sui);
          } else {
            sui = ui.windows.get(senderInfo);
            sui.setVisible(true);
          }
          sui.updateHistory(senderInfo, msgStr);
        }
        //刷新
        else if (msg.getClass() == ServerRefreshFriendsMessage.class) {
          ServerRefreshFriendsMessage m = (ServerRefreshFriendsMessage) msg;
          byte[] bytes = m.getFriendsBytes();
          List<String> friends = (List<String>) Util.deSerializeObject(bytes);
          ui.refreshFriends(friends);
        }
      } catch (Exception e) {
        return;
      }
    }
  }

  /**
   * 发送消息
   */
  public void sendMessage(byte[] bytes) {
    try {
      sock.getOutputStream().write(bytes);
      sock.getOutputStream().flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void close() {
    try {
      sock.getOutputStream().close();
      sock.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}