package com.xiangshi.archiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Archiver {

  // 归档
  public static void main(String[] args) throws Exception {
    FileOutputStream fos = new FileOutputStream("data/input/u.xar", true);
//    fos.write(addFile("xx.pdf"));
//    fos.write(addFile("yy.png"));
//    fos.write(addFile("zz.txt"));
    fos.write(addFile("data/input/vv.ppt"));
    fos.close();
  }

  /**
   * 归档 将这个文件加入到总文件的byte array里
   */
  public static byte[] addFile(String path) throws Exception {
    File file = new File(path);

    // 文件名的bytes数组 及其长度
    String fileName = file.getName();
    byte[] fileNameBytesArr = fileName.getBytes();
    int fileNameBytesArrLen = fileNameBytesArr.length;

    // 文件内容的bytes数组 及其长度
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    FileInputStream fis = new FileInputStream(file);
    byte[] buffer = new byte[1024];  // 缓冲 用来读输入流里的字节数据
    int numOfBytesRead = -1;
    while ((numOfBytesRead = fis.read(buffer)) != -1) {
      baos.write(buffer, 0, numOfBytesRead);
    }
    fis.close();  // 释放资源
    // 得到文件内容
    byte[] fileContentArr = baos.toByteArray();
    // 得到文件内容的字节长度
    int fileContentLen = (int) file.length();

    int total = 4 + fileNameBytesArrLen + 4 + fileContentLen;

    byte[] bytes = new byte[total];

    // 拷贝 文件名的字节长度(int)的字节形式 to byte数组
    System.arraycopy(Util.int2Bytes(fileNameBytesArrLen), 0, bytes, 0, 4);

    // 拷贝 文件名内容的字节形式 to byte数组
    System.arraycopy(fileNameBytesArr, 0, bytes, 4, fileNameBytesArrLen);

    // 拷贝 文件内容长度(int)的字节形式 to byte数组
    System.arraycopy(Util.int2Bytes(fileContentLen), 0, bytes, 4 + fileNameBytesArrLen, 4);

    // 拷贝 文件内容的字节形式 to byte数组
    System.arraycopy(fileContentArr, 0, bytes, 4 + fileNameBytesArrLen + 4, fileContentArr.length);

    System.out.println("byte[] fileContentArr 的长度是：" + fileContentArr.length);
    System.out.println("(int) file.length()   的长度是：" + fileContentLen);

    return bytes;
  }
}
