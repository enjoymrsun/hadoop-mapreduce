package com.xiangshi.nio;

import org.junit.Test;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {

  @Test
  public void testFileMappingMemory() throws Exception {
    File f = new File("data/data.txt");
    RandomAccessFile raf = new RandomAccessFile(f, "rw");
    //12 0-11
    MappedByteBuffer buf = raf.getChannel().map(FileChannel.MapMode.READ_WRITE, 1, 10);
    // 通过文件内存的映射 修改内存buffer中的数据，便直接修改了文件中的数据，IO更快速
    System.out.println((char) buf.get(0));
    System.out.println((char) buf.get(9));
    System.out.println(buf.capacity());
    buf.put(0, (byte) 's');
  }

  /**
   * 测试离堆缓冲区
   */
  @Test
  public void testOffHeapBuffer() {
    System.out.println("xxx");
    ByteBuffer.allocate(500 * 1024 * 1024);
    ByteBuffer.allocateDirect(500 * 1024 * 1024);  // 不会加到JVM中，直接加到系统中
    System.out.println("xxx");
  }

  /**
   * 通过反射机制 将allocateDirect的内存回收回来 但是很危险，因为你很可能继续使用已经回收过的内存！！
   */
  @Test
  public void testOffHeapBuf2() throws Exception {
    // allocateDirect 1个G的空间
    ByteBuffer buf = ByteBuffer.allocateDirect(1024 * 1024 * 1024);

    // 得到类描述符
    Class clazz = Class.forName("java.nio.DirectByteBuffer");

    // 通过类描述符查找指定字段(字段描述符)
    Field f = clazz.getDeclaredField("cleaner");
    // 设置可访问性
    f.setAccessible(true);

    // 取得f 在buf对象 上的值
    Object cleaner = f.get(buf);

    System.out.println(cleaner);
    Class cleanerClass = Class.forName("sun.misc.Cleaner");
    Method cleanMethod = cleanerClass.getDeclaredMethod("clean");
    cleanMethod.invoke(cleaner);

    System.out.println("kkkkk");
    System.out.println("kkkkk");
    System.out.println("kkkkk");

    // !!!危险操作!!!  访问了已经被回收的系统内存
    System.out.println(buf.get(0));
    System.out.println("kkkkk");

  }
}
