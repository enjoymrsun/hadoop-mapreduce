package com.xiangshi.network.tcp.qq.common;

/**
 * 客户端单聊消息
 */
public class ClientSingleChatMessage extends Message {

  //发送方消息
  private byte[] recverInfoBytes;

  public byte[] getRecverInfoBytes() {
    return recverInfoBytes;
  }

  public void setRecverInfoBytes(byte[] recverInfoBytes) {
    this.recverInfoBytes = recverInfoBytes;
  }

  //消息内容
  private byte[] message;

  public byte[] getMessage() {
    return message;
  }

  public void setMessage(byte[] message) {
    this.message = message;
  }

  public int getMessageType() {
    return Message.CLIENT_TO_SERVER_SINGLE_CHAT;
  }
}