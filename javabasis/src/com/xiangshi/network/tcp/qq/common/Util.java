package com.xiangshi.network.tcp.qq.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

public class Util {

  /**
   * 将long型数据转换成byte[]
   */
  public static byte[] long2Bytes(long l) {
    byte[] bytes = new byte[8];
    bytes[0] = (byte) l;
    bytes[1] = (byte) (l >> 8);
    bytes[2] = (byte) (l >> 16);
    bytes[3] = (byte) (l >> 24);
    bytes[4] = (byte) (l >> 32);
    bytes[5] = (byte) (l >> 40);
    bytes[6] = (byte) (l >> 48);
    bytes[7] = (byte) (l >> 56);
    return bytes;
  }

  public static long byte2Long(byte[] bytes) {
    return ((long) (bytes[0] & 0xFF))
            | ((long) (bytes[1] & 0xFF) << 8)
            | ((long) (bytes[2] & 0xFF) << 16)
            | ((long) (bytes[3] & 0xFF) << 24)
            | ((long) (bytes[4] & 0xFF) << 32)
            | ((long) (bytes[5] & 0xFF) << 40)
            | ((long) (bytes[6] & 0xFF) << 48)
            | ((long) (bytes[7] & 0xFF) << 56);
  }

  /**
   * 将long型数据转换成byte[]
   */
  public static byte[] int2Bytes(int l) {
    byte[] bytes = new byte[4];
    bytes[0] = (byte) l;
    bytes[1] = (byte) (l >> 8);
    bytes[2] = (byte) (l >> 16);
    bytes[3] = (byte) (l >> 24);
    return bytes;
  }

  public static int bytes2Int(byte[] bytes) {
    return ((bytes[0] & 0xFF))
            | ((bytes[1] & 0xFF) << 8)
            | ((bytes[2] & 0xFF) << 16)
            | ((bytes[3] & 0xFF) << 24);
  }

  public static int byte2Int(byte[] bytes, int offset) {
    return ((bytes[0 + offset] & 0xFF))
            | ((bytes[1 + offset] & 0xFF) << 8)
            | ((bytes[2 + offset] & 0xFF) << 16)
            | ((bytes[3 + offset] & 0xFF) << 24);
  }

  /**
   * 串行化对象
   */
  public static byte[] serializeObject(Serializable src) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      oos.writeObject(src);
      oos.close();
      baos.close();
      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 使用java的反串行化实现好友列表的刷新
   */
  public static Serializable deSerializeObject(byte[] bytes) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
      ObjectInputStream ois = new ObjectInputStream(bais);
      Serializable o = (Serializable) ois.readObject();
      ois.close();
      bais.close();
      return o;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 通过sock得到远程客户端user信息
   */
  public static byte[] getUserInfo(Socket sock) {
    try {
      InetSocketAddress addr = (InetSocketAddress) sock.getRemoteSocketAddress();
      String ip = addr.getAddress().getHostAddress();
      int port = addr.getPort();
      return (ip + ":" + port).getBytes();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 通过sock得到远程客户端user信息
   */
  public static String getUserInfoStr(Socket sock) {
    try {
      InetSocketAddress addr = (InetSocketAddress) sock.getRemoteSocketAddress();
      String ip = addr.getAddress().getHostAddress();
      int port = addr.getPort();
      return ip + ":" + port;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
