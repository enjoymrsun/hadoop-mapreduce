package com.xiangshi.network.tcp.qq.common;

/**
 * 消息的抽象类
 */
public abstract class Message {

  /* 客户端发送给服务器的消息 */
  public final static int CLIENT_TO_SERVER_SINGLE_CHAT = 0;
  public final static int CLIENT_TO_SERVER_CHATS = 1;
  public final static int CLIENT_TO_SERVER_REFRESH_FRIENDS = 2;
  public final static int CLIENT_TO_SERVER_EXIT = 3;

  /* 服务器发送给客户端的消息 */
  public final static int SERVER_TO_CLIENT_REFRESH_FRIENTS = 11;
  public final static int SERVER_TO_CLIENT_SINGLE_CHAT = 12;
  public final static int SERVER_TO_CLIENT_CHATS = 13;

  public abstract int getMessageType();

}
