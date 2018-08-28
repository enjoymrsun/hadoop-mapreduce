package com.xiangshi.network.udp.screenbroadcast;

import java.io.ByteArrayOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.HashMap;

public class BroadcastReceiver extends Thread {

  private DatagramSocket receiverSocket;
  private DatagramPacket packet;
  private StudentUI ui;
  private HashMap<Integer, FrameUnit> image;

  public BroadcastReceiver(StudentUI ui) {
    try {
      this.ui = ui;
      receiverSocket = new DatagramSocket(9999);
      System.out.println("receiverSocket new了几次：" + 1);
      packet = new DatagramPacket(new byte[Util.getMaxSinglePacketDataVolume() + 14],
              Util.getMaxSinglePacketDataVolume() + 14);
      image = new HashMap<>();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void run() {
    try {
      long currentTime = 0;
      while (true) {
        receiverSocket.receive(packet);

        // 判断packet数据是否过期
        byte[] bytes = new byte[packet.getLength()];
        System.arraycopy(packet.getData(), 0, bytes, 0, packet.getLength());
        FrameUnit unit = bytes2FrameUnit(bytes);

        if (unit.getScreenshotId() > currentTime) {
          image.clear();
          image.put(unit.getUnitNo(), unit);
          currentTime = unit.getScreenshotId();
        } else if (unit.getScreenshotId() == currentTime) {
          image.put(unit.getUnitNo(), unit);
        }

        // 处理image
        if (unit.getScreenshotId() >= currentTime && image.size() == unit.getUnitCount()) {
          // 处理HashMap 生成byte[]
          byte[] compressedImageData = processFrameUnits();

          // 解压缩 恢复成原来的数据 将IO密集型转为CPU密集型
          byte[] unCompressedImageData = Util.unCompressData(compressedImageData);

          // 更新
          ui.updateIcon(unCompressedImageData);
          image.clear();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private FrameUnit bytes2FrameUnit(byte[] bytes) {
    FrameUnit unit = new FrameUnit();

    long screenshotId = Util.bytes2Long(bytes);
    int unitsCount = bytes[8] & 0xFF;
    int unitNo = bytes[9] & 0xFF;
    int dataLen = Util.bytes2Int(bytes, 10);
    byte[] originalData = new byte[dataLen];
    System.arraycopy(bytes, 14, originalData, 0, dataLen);

    unit.setScreenshotId(screenshotId);
    unit.setUnitCount(unitsCount);
    unit.setUnitNo(unitNo);
    unit.setDataLen(dataLen);
    unit.setData(originalData);

    return unit;
  }

  private byte[] processFrameUnits() {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();

      int unitsCount = image.values().iterator().next().getUnitCount();

      for (int i = 0; i < unitsCount; i++) {
        FrameUnit u = image.get(i);
        baos.write(u.getData());
      }
      baos.close();

      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }


}
