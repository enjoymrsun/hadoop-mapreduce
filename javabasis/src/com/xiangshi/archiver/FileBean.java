package com.xiangshi.archiver;

public class FileBean {
  private String fileName;
  private byte[] fileContent;

  public FileBean(String fileName, byte[] fileContent) {
    this.fileName = fileName;
    this.fileContent = fileContent;
  }

  public FileBean() {
  }

  public String getFileName() {
    return fileName;
  }

  public byte[] getFileContent() {
    return fileContent;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public void setFileContent(byte[] fileContent) {
    this.fileContent = fileContent;
  }
}
