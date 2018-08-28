package com.xiangshi.network.tcp.qq.server;

import com.xiangshi.network.tcp.qq.common.Message;
import com.xiangshi.network.tcp.qq.common.Util;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Server类
 */
public class QQServer {

  private static QQServer server = new QQServer();

  public static QQServer getInstance() {
    return server;
  }

  private QQServer() {
  }

  //维护所有socket对象
  //key : remoteIP + ":" + remotePort
  private Map<String, Socket> allSockets = new HashMap<String, Socket>();

  /**
   * 启动服务器
   */
  public void start() {
    try {
      ServerSocket ss = new ServerSocket(8888);
      while (true) {
        //阻塞
        Socket sock = ss.accept();

        InetSocketAddress remoteAddr = (InetSocketAddress) sock.getRemoteSocketAddress();
        //远程ip
        String remoteIp = remoteAddr.getAddress().getHostAddress();
        //远程端口
        int remotePort = remoteAddr.getPort();
        String key = remoteIp + ":" + remotePort;
        allSockets.put(key, sock);

        //开起服务器端通信线程
        new CommThread(sock).start();
        this.broadcastFriends();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 得到好友列表的串行数据
   */
  public byte[] getFriendBytes() {
    try {
      List<String> list = new ArrayList<String>(allSockets.keySet());
      return Util.serializeObject((Serializable) list);

    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * 广播群聊
   */
  public void broadcast(byte[] bytes) {
    Iterator<Socket> it = allSockets.values().iterator();
    while (it.hasNext()) {
      try {
        OutputStream out = it.next().getOutputStream();
        //1.消息类型
        out.write(bytes);
        out.flush();
      } catch (Exception e) {
        continue;
      }
    }
  }

  /**
   * 发送私聊
   */
  public void send(byte[] msg, byte[] userInfo) {
    try {
      String key = new String(userInfo);
      if (allSockets.containsKey(key)) {
        OutputStream out = allSockets.get(key).getOutputStream();
        out.write(msg);
        out.flush();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * 广播好友列表
   */
  public void broadcastFriends() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      //1.刷新列表消息类型
      baos.write(Util.int2Bytes(Message.SERVER_TO_CLIENT_REFRESH_FRIENTS));
      //2.列表的数据长度
      byte[] friendsBytes = QQServer.getInstance().getFriendBytes();
      baos.write(Util.int2Bytes(friendsBytes.length));
      //3.列表数据
      baos.write(QQServer.getInstance().getFriendBytes());
      QQServer.getInstance().broadcast(baos.toByteArray());

    } catch (Exception e) {
    }
  }

  /**
   * 删除指定用户
   */
  public synchronized void removeUser(String user) {
    allSockets.remove(user);
  }
}