package com.xiangshi.network.tcp.qq.common;

/**
 * 服务器刷新列表
 */
public class ServerRefreshFriendsMessage extends Message {

  private byte[] friendsBytes;

  public byte[] getFriendsBytes() {
    return friendsBytes;
  }

  public void setFriendsBytes(byte[] friendsBytes) {
    this.friendsBytes = friendsBytes;
  }

  public int getMessageType() {
    return SERVER_TO_CLIENT_REFRESH_FRIENTS;
  }

}
