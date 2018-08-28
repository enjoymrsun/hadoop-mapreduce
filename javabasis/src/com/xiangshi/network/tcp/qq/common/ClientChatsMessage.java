package com.xiangshi.network.tcp.qq.common;

/**
 * 客户端和大家群聊消息
 */
public class ClientChatsMessage extends Message {
  //消息内容
  private byte[] message;

  public byte[] getMessage() {
    return message;
  }

  public void setMessage(byte[] message) {
    this.message = message;
  }

  public int getMessageType() {
    return CLIENT_TO_SERVER_CHATS;
  }
}
