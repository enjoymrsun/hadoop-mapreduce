package com.xiangshi.archiver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class Unarchiver {

  // 解归档
  public static void main(String[] args) throws Exception {
    FileInputStream fis = new FileInputStream("data/input/u.xar");
    List<FileBean> files = new ArrayList<>();

    FileBean fb = null;

    while ((fb = generateNextFile(fis)) != null) {
      files.add(fb);
    }
    fis.close();

    FileOutputStream fos = null;
    for (FileBean f : files) {
      fos = new FileOutputStream("data/output/" + f.getFileName());
      fos.write(f.getFileContent());  // 不写这一步 不会把文件数据输入进去
      fos.close();
    }
  }

  // 解归档 生成文件FileBean
  public static FileBean generateNextFile(FileInputStream fis) throws Exception {
    // 文件名
    byte[] fileNameLengthArr = new byte[4];
    int res = fis.read(fileNameLengthArr);
    if (res == -1) {
      return null;
    }

    int fileNameArrLen = Util.bytes2Int(fileNameLengthArr);
    byte[] fileNameArr = new byte[fileNameArrLen];
    fis.read(fileNameArr);

    String fileName = new String(fileNameArr);

    // 文件内容
    byte[] fileContentLengthArr = new byte[4];
    fis.read(fileContentLengthArr);
    int fileContentArrLen = Util.bytes2Int(fileContentLengthArr);

    byte[] fileContentArr = new byte[fileContentArrLen];
    fis.read(fileContentArr);

    return new FileBean(fileName, fileContentArr);
  }
}
