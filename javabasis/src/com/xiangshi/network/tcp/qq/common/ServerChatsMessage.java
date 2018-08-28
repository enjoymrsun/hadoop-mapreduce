package com.xiangshi.network.tcp.qq.common;

/**
 * 服务器群聊消息
 */
public class ServerChatsMessage extends Message {

  private byte[] senderInfoBytes;

  private byte[] msgBytes;

  public byte[] getSenderInfoBytes() {
    return senderInfoBytes;
  }


  public void setSenderInfoBytes(byte[] senderInfoBytes) {
    this.senderInfoBytes = senderInfoBytes;
  }


  public byte[] getMsgBytes() {
    return msgBytes;
  }


  public void setMsgBytes(byte[] msgBytes) {
    this.msgBytes = msgBytes;
  }

  public int getMessageType() {
    return SERVER_TO_CLIENT_CHATS;
  }
}