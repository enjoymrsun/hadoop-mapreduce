package com.xiangshi.network.tcp.qq.common;

/**
 * 客户端退出消息
 */
public class ClientRefreshFriendsMessage extends Message {

  public int getMessageType() {
    return CLIENT_TO_SERVER_REFRESH_FRIENDS;
  }

}