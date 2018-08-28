package com.xiangshi.network.tcp.qq.common;

import com.xiangshi.network.tcp.qq.server.QQServer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;


/**
 * 消息工厂
 */
public class MessageFactory {
  /**
   * 从流中解析消息 ,解析客户端消息，并直接转换成服务器消息
   */
  public static byte[] parseClientMessageAndSend(Socket sock) throws Exception {
    InputStream in = sock.getInputStream();

    byte[] msgTypeBytes = new byte[4];
    in.read(msgTypeBytes);
    //消息
    Message msg = null;

    switch (Util.bytes2Int(msgTypeBytes)) {
      //1.群聊
      case Message.CLIENT_TO_SERVER_CHATS: {
        //构造消息对象
        msg = new ClientChatsMessage();
        //读取消息长度4字节
        byte[] bytes4 = new byte[4];
        in.read(bytes4);
        int msgLen = Util.bytes2Int(bytes4);

        //读取消息内容
        byte[] msgBytes = new byte[msgLen];
        in.read(msgBytes);
        ((ClientChatsMessage) msg).setMessage(msgBytes);

        //转换成服务器消息
        ServerChatsMessage serverMsg = new ServerChatsMessage();
        serverMsg.setSenderInfoBytes(Util.getUserInfo(sock));
        serverMsg.setMsgBytes(((ClientChatsMessage) msg).getMessage());

        //转换成数组
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //消息类型(4)
        baos.write(Util.int2Bytes(Message.SERVER_TO_CLIENT_CHATS));
        //userInfoLen(4)
        baos.write(Util.int2Bytes(serverMsg.getSenderInfoBytes().length));
        //userInfo
        baos.write(serverMsg.getSenderInfoBytes());
        //msgLen(4)
        baos.write(Util.int2Bytes(serverMsg.getMsgBytes().length));
        //msg
        baos.write(serverMsg.getMsgBytes());

        //广播
        QQServer.getInstance().broadcast(baos.toByteArray());
      }
      break;
      //exit
      case Message.CLIENT_TO_SERVER_EXIT: {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.刷新列表消息类型
        baos.write(Util.int2Bytes(Message.SERVER_TO_CLIENT_REFRESH_FRIENTS));
        //2.列表的数据长度
        baos.write(Util.int2Bytes(QQServer.getInstance().getFriendBytes().length));
        //3.列表数据
        baos.write(QQServer.getInstance().getFriendBytes());
        QQServer.getInstance().broadcast(baos.toByteArray());
      }
      break;
      //刷新列表
      case Message.CLIENT_TO_SERVER_REFRESH_FRIENDS: {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.刷新列表消息类型
        baos.write(Util.int2Bytes(Message.SERVER_TO_CLIENT_REFRESH_FRIENTS));
        //2.列表的数据长度
        baos.write(Util.int2Bytes(QQServer.getInstance().getFriendBytes().length));
        //3.列表数据
        baos.write(QQServer.getInstance().getFriendBytes());
        QQServer.getInstance().broadcast(baos.toByteArray());
        //发送好友列表给client
        QQServer.getInstance().send(baos.toByteArray(), Util.getUserInfo(sock));
      }
      break;
      //私聊
      case Message.CLIENT_TO_SERVER_SINGLE_CHAT: {
        //构造消息对象
        msg = new ClientSingleChatMessage();

        byte[] bytes4 = new byte[4];
        //读取接受者用户信息长度
        in.read(bytes4);
        int recverInfoLen = Util.bytes2Int(bytes4);

        //读取接受者用户信息
        byte[] recvInfoBytes = new byte[recverInfoLen];
        in.read(recvInfoBytes);
        ((ClientSingleChatMessage) msg).setRecverInfoBytes(recvInfoBytes);

        //读取消息长度4字节
        in.read(bytes4);
        int msgLen = Util.bytes2Int(bytes4);

        //读取消息内容
        byte[] msgBytes = new byte[msgLen];
        in.read(msgBytes);
        ((ClientSingleChatMessage) msg).setMessage(msgBytes);

        //转换成服务器私聊消息
        ServerSingleChatMessage serverMsg = new ServerSingleChatMessage();

        //发送者消息
        serverMsg.setSenderInfoBytes(Util.getUserInfo(sock));

        //接受者消息
        serverMsg.setRecverInfoBytes(((ClientSingleChatMessage) msg).getRecverInfoBytes());

        //聊天信息
        serverMsg.setMessage(((ClientSingleChatMessage) msg).getMessage());

        //构造消息
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //1.类型
        baos.write(Util.int2Bytes(Message.SERVER_TO_CLIENT_SINGLE_CHAT));
        //2.senderInfoLen
        baos.write(Util.int2Bytes(serverMsg.getSenderInfoBytes().length));
        //3.senderInfo
        baos.write(serverMsg.getSenderInfoBytes());
        //4.msgLen
        baos.write(Util.int2Bytes(serverMsg.getMessage().length));
        baos.write(serverMsg.getMessage());

        //发送私聊给接受者
        QQServer.getInstance().send(baos.toByteArray(), serverMsg.getRecverInfoBytes());
      }
      break;
    }
    return null;
  }

  /**
   * 解析服务器端消息
   */
  public static Message parseServerMessage(Socket sock) throws IOException {
    InputStream in = sock.getInputStream();
    byte[] msgTypeBytes = new byte[4];
    in.read(msgTypeBytes);

    int msgType = Util.bytes2Int(msgTypeBytes);
    //消息
    switch (msgType) {
      //群聊
      case Message.SERVER_TO_CLIENT_CHATS: {
        //1.senderInfoLen
        byte[] bytes4 = new byte[4];
        in.read(bytes4);
        int senderInfoLen = Util.bytes2Int(bytes4);

        //senderInfo
        byte[] senderInfoBytes = new byte[senderInfoLen];
        in.read(senderInfoBytes);

        //msgLen
        in.read(bytes4);
        int msgLen = Util.bytes2Int(bytes4);

        //msg
        byte[] msgBytes = new byte[msgLen];
        in.read(msgBytes);

        //构造服务器群聊消息
        ServerChatsMessage msg = new ServerChatsMessage();
        msg.setMsgBytes(msgBytes);
        msg.setSenderInfoBytes(senderInfoBytes);
        return msg;
      }
      //私聊
      case Message.SERVER_TO_CLIENT_SINGLE_CHAT: {
        //1.senderInfoLen
        byte[] bytes4 = new byte[4];
        in.read(bytes4);
        int senderInfoLen = Util.bytes2Int(bytes4);

        //senderInfo
        byte[] senderInfoBytes = new byte[senderInfoLen];
        in.read(senderInfoBytes);

        //msgLen
        in.read(bytes4);
        int msgLen = Util.bytes2Int(bytes4);

        //msg
        byte[] msgBytes = new byte[msgLen];
        in.read(msgBytes);

        //构造服务器群聊消息
        ServerSingleChatMessage msg = new ServerSingleChatMessage();
        msg.setMessage(msgBytes);
        msg.setSenderInfoBytes(senderInfoBytes);
        return msg;
      }
      //刷新列表
      case Message.SERVER_TO_CLIENT_REFRESH_FRIENTS: {
        //1.friendsLen
        byte[] bytes4 = new byte[4];
        in.read(bytes4);
        int friendsLen = Util.bytes2Int(bytes4);

        //friends
        byte[] friendsBytes = new byte[friendsLen];
        in.read(friendsBytes);

        //构造服务器群聊消息
        ServerRefreshFriendsMessage msg = new ServerRefreshFriendsMessage();
        msg.setFriendsBytes(friendsBytes);
        return msg;
      }
    }
    return null;
  }

  /**
   * 组装客户端群聊消息
   */
  public static byte[] popClientChatsMessage(String txt) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      //1.消息类型
      baos.write(Util.int2Bytes(Message.CLIENT_TO_SERVER_CHATS));

      byte[] msgBytes = txt.getBytes();
      //2.消息长度
      baos.write(Util.int2Bytes(msgBytes.length));
      //3.消息
      baos.write(msgBytes);
      baos.close();
      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 组装私聊消息
   */
  public static byte[] popClientSingleChatMessage(String recvInfo, String str) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      //1.消息类型
      baos.write(Util.int2Bytes(Message.CLIENT_TO_SERVER_SINGLE_CHAT));

      //2.接受者新长度
      byte[] recvInfoBytes = recvInfo.getBytes();
      baos.write(Util.int2Bytes(recvInfoBytes.length));

      //3.接受者信息
      baos.write(recvInfoBytes);

      //4.消息
      byte[] msgBytes = str.getBytes();

      //5.消息长度
      baos.write(Util.int2Bytes(msgBytes.length));

      //6.消息
      baos.write(msgBytes);
      baos.close();
      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;

  }

  public static byte[] popClientRefreshFriendsMessage() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      //1.消息类型
      baos.write(Util.int2Bytes(Message.CLIENT_TO_SERVER_REFRESH_FRIENDS));
      baos.close();
      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}