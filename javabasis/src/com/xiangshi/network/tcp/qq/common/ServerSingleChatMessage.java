package com.xiangshi.network.tcp.qq.common;

/**
 * 服务器私聊
 */
public class ServerSingleChatMessage extends Message {

  //发送方消息
  private byte[] recverInfoBytes;

  private byte[] senderInfoBytes;

  public byte[] getSenderInfoBytes() {
    return senderInfoBytes;
  }

  public void setSenderInfoBytes(byte[] senderInfoBytes) {
    this.senderInfoBytes = senderInfoBytes;
  }

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
    return Message.SERVER_TO_CLIENT_SINGLE_CHAT;
  }
}
