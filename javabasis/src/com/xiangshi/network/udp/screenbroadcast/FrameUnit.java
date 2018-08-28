package com.xiangshi.network.udp.screenbroadcast;

public class FrameUnit {
  private long screenshotId;
  private int unitCount;
  private int unitNo;
  private int dataLen;
  private byte[] data;

  public long getScreenshotId() {
    return screenshotId;
  }

  public int getUnitCount() {
    return unitCount;
  }

  public int getUnitNo() {
    return unitNo;
  }

  public int getDataLen() {
    return dataLen;
  }

  public byte[] getData() {
    return data;
  }

  public void setScreenshotId(long screenshotId) {
    this.screenshotId = screenshotId;
  }

  public void setUnitCount(int unitCount) {
    this.unitCount = unitCount;
  }

  public void setUnitNo(int unitNo) {
    this.unitNo = unitNo;
  }

  public void setDataLen(int dataLen) {
    this.dataLen = dataLen;
  }

  public void setData(byte[] data) {
    this.data = data;
  }
}
