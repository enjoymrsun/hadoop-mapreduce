package com.xiangshi.archiver;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipTest {

  /**
   * 测试压缩功能
   */
  @Test
  public void testZip() throws Exception {
    // 文件输出流, 要生成一个叫"yasuo.xar"的 压缩文件
    FileOutputStream fos = new FileOutputStream("data/output/yasuo.xar");

    // 压缩输出流
    ZipOutputStream zos = new ZipOutputStream(fos);

    // 对这4个文件进行压缩
    String[] files = new String[]{
            "data/input/vv.ppt",
            "data/input/xx.pdf",
            "data/input/yy.png",
            "data/input/zz.txt"
    };

    // 循环向zos添加entry条目
    for (String path : files) {
      addFile(zos, path);
    }

    // 因为zos 使用了fos 所以应该先关闭调用者
    zos.close();
    // 之后再关闭被调用者
    fos.close();
    System.out.println("successfully finish");
  }

  /**
   * 向压缩文件加入要压缩的文件
   */
  public void addFile(ZipOutputStream zos, String path) throws Exception {
    File file = new File(path);
    FileInputStream fis = new FileInputStream(file);
    byte[] content = new byte[fis.available()];
    fis.read(content);
    fis.close();

    zos.putNextEntry(new ZipEntry(file.getName()));
    zos.write(content);
    zos.closeEntry();

  }

  /**
   *
   */
  @Test
  public void testUnzip() throws Exception {
    // 文件输入流
    FileInputStream fis = new FileInputStream("data/output/yasuo.zip");
    // 压缩输入流
    ZipInputStream zis = new ZipInputStream(fis);

    ZipEntry e = null;
    while ((e = zis.getNextEntry()) != null) {
      String fileName = e.getName();
      FileOutputStream fos = new FileOutputStream("data/output/unzip/" + fileName);

      byte[] buffer = new byte[1024];
      int byteLen = 0;
      while ((byteLen = zis.read(buffer)) != -1) {
        fos.write(buffer, 0, byteLen);
      }

      // 文件写完了 把输出流关上
      fos.close();
    }

    zis.close();
    fis.close();
  }
}
