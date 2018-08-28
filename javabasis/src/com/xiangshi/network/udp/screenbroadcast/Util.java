package com.xiangshi.network.udp.screenbroadcast;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

public class Util {
  private static Robot robot;

  static {
    try {
      robot = new Robot();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Java 截屏程序 生成Image的字节数组
   */
  public static byte[] createScreenshot() {
    try {
      BufferedImage image = robot.createScreenCapture(new Rectangle(0, 0, 1450, 910));
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ImageIO.write(image, "jpg", baos);
      baos.close();

      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static byte[] long2Bytes(long l) {
    byte[] bytes = new byte[8];
    for (int i = 0; i < 8; i++) {
      bytes[i] = (byte) (l >> (i * 8));
    }

    return bytes;
  }

  public static long bytes2Long(byte[] bytes) {
    long l0 = (long) (bytes[0] & 0xFF);
    long l1 = ((long) (bytes[1] & 0xFF)) << (1 * 8);
    long l2 = ((long) (bytes[2] & 0xFF)) << (2 * 8);
    long l3 = ((long) (bytes[3] & 0xFF)) << (3 * 8);
    long l4 = ((long) (bytes[4] & 0xFF)) << (4 * 8);
    long l5 = ((long) (bytes[5] & 0xFF)) << (5 * 8);
    long l6 = ((long) (bytes[6] & 0xFF)) << (6 * 8);
    long l7 = ((long) (bytes[7] & 0xFF)) << (7 * 8);

    return l0 | l1 | l2 | l3 | l4 | l5 | l6 | l7;
  }

  public static byte[] int2Bytes(int intI) {
    byte[] bytes = new byte[4];
    for (int idx = 0; idx < 4; idx++) {
      bytes[idx] = (byte) (intI >> (idx * 8));
    }

    return bytes;
  }

  public static int bytes2Int(byte[] bytes, int offset) {
    int i0 = bytes[0 + offset] & 0xFF;
    int i1 = (bytes[1 + offset] & 0xFF) << (1 * 8);
    int i2 = (bytes[2 + offset] & 0xFF) << (2 * 8);
    int i3 = (bytes[3 + offset] & 0xFF) << (3 * 8);

    return i0 | i1 | i2 | i3;
  }

  public static byte[] compressData(byte[] data) {
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ZipOutputStream zos = new ZipOutputStream(baos);

      zos.putNextEntry(new ZipEntry("onlyOne"));
      zos.write(data);
      zos.closeEntry();

      zos.close();
      baos.close();

      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static byte[] unCompressData(byte[] compressedData) {
    try {
      ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
      ZipInputStream zis = new ZipInputStream(bais);

      ZipEntry e = null;
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      while ((e = zis.getNextEntry()) != null) {

        byte[] buffer = new byte[1024];
        int bufferLen = 0;
        while ((bufferLen = zis.read(buffer)) != -1) {
          baos.write(buffer, 0, bufferLen);
        }
      }

      baos.close();
      zis.close();
      bais.close();

      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return null;
  }

  public static int getMaxSinglePacketDataVolume() {
    return 60 * 1024;
  }


}
