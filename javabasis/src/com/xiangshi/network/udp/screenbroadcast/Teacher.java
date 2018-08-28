package com.xiangshi.network.udp.screenbroadcast;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.util.LinkedList;
import java.util.List;

public class Teacher {
  private static DatagramSocket senderSocket = null;
  private int i = 0;

  static {
    try {
      senderSocket = new DatagramSocket(8888);
      System.out.println("senderSocket new了几次：" + 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    while (true) {
      sendOneImage();
    }
  }

  private static void sendOneImage() {
    try {
      // 1. 获取截屏数据
      byte[] screenshotData = Util.createScreenshot();

      // 2. 压缩截屏数据
      byte[] compressedScreenshotData = Util.compressData(screenshotData);

      // 3. 分割截屏数据 into FrameUnits
      List<FrameUnit> units = splitData(compressedScreenshotData);

      // 4. 将每个FrameUnit 打包进DatagramPacket 里发送出去
      sendFrameUnits(units);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static List<FrameUnit> splitData(byte[] data) {
    List<FrameUnit> list = new LinkedList<>();

    long timeId = System.currentTimeMillis();

    int remainDataOfLastUnit = data.length % Util.getMaxSinglePacketDataVolume();
    int unitsCount = data.length / Util.getMaxSinglePacketDataVolume();
    if (remainDataOfLastUnit != 0) {
      unitsCount++;
    }

    for (int i = 0; i < unitsCount; i++) {
      if (i == unitsCount - 1) {
        byte[] pieceData;

        if (remainDataOfLastUnit == 0) {
          pieceData = new byte[Util.getMaxSinglePacketDataVolume()];
          System.arraycopy(data, i * Util.getMaxSinglePacketDataVolume(), pieceData, 0, Util.getMaxSinglePacketDataVolume());
        } else {
          pieceData = new byte[remainDataOfLastUnit];
          System.arraycopy(data, i * Util.getMaxSinglePacketDataVolume(), pieceData, 0, remainDataOfLastUnit);
        }

        FrameUnit u = data2FrameUnit(pieceData, pieceData.length, timeId, unitsCount, i);
        list.add(u);
      } else {
        byte[] pieceData = new byte[Util.getMaxSinglePacketDataVolume()];
        System.arraycopy(data, i * Util.getMaxSinglePacketDataVolume(), pieceData, 0, Util.getMaxSinglePacketDataVolume());

        FrameUnit u = data2FrameUnit(pieceData, pieceData.length, timeId, unitsCount, i);
        list.add(u);
      }
    }

    return list;
  }

  private static FrameUnit data2FrameUnit(byte[] data, int dataLen, long screenshotId, int unitCount, int unitNo) {
    FrameUnit unit = new FrameUnit();

    unit.setData(data);
    unit.setDataLen(dataLen);
    unit.setScreenshotId(screenshotId);
    unit.setUnitCount(unitCount);
    unit.setUnitNo(unitNo);

    return unit;
  }

  private static void sendFrameUnits(List<FrameUnit> units) {
    try {
      for (FrameUnit unit : units) {
        byte[] data = frameUnit2Bytes(unit);
        DatagramPacket packet = new DatagramPacket(data, data.length);
        packet.setSocketAddress(new InetSocketAddress("localhost", 9999));
        senderSocket.send(packet);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static byte[] frameUnit2Bytes(FrameUnit unit) {
    // 不同unit长度不同
    byte[] data = new byte[unit.getDataLen() + 8 + 1 + 1 + 4];

    // screenshotId 放入字符数组中
    byte[] frameIdBytes = Util.long2Bytes(unit.getScreenshotId());
    System.arraycopy(frameIdBytes, 0, data, 0, frameIdBytes.length);

    // unit count 放入字符数组中
    data[8] = (byte) (unit.getUnitCount());

    // unitNo 放入字符数组中
    data[9] = (byte) (unit.getUnitNo());

    // data length 放入字符数组中
    byte[] dataLengthBytes = Util.int2Bytes(unit.getDataLen());
    System.arraycopy(dataLengthBytes, 0, data, 10, dataLengthBytes.length);

    // copy data
    byte[] originalData = unit.getData();
    System.arraycopy(originalData, 0, data, 14, unit.getDataLen());

    return data;
  }

}
