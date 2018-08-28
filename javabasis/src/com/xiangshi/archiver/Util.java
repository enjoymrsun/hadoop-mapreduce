package com.xiangshi.archiver;

public class Util {

  public static byte[] int2Bytes(int i) {
    byte[] arr = new byte[4];
    arr[0] = (byte) i;
    arr[1] = (byte) (i >> 8);
    arr[2] = (byte) (i >> 16);
    arr[3] = (byte) (i >> 24);

    return arr;
  }

  public static int bytes2Int(byte[] bytes) {
    // (原来的写法是错的 因为：如果bytes[0]的最大的一个bit是1 那么补成int后 i0的前24个bits都是1) 或运算没意义
    // int i0 = bytes[0]; (原来的写法)
    int i0 = bytes[0] & 0xFF;
    int i1 = (bytes[1] & 0xFF) << 8;
    int i2 = (bytes[2] & 0xFF) << 16;
    int i3 = (bytes[3] & 0xFF) << 24;

    return i0 | i1 | i2 | i3;
  }
}
