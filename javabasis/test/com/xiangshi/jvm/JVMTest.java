package com.xiangshi.jvm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.xiangshi.jvm.CallSelfMain.callSelf;

public class JVMTest {

  @Test
  public void testStackOverflow() {
    callSelf(1);
  }

  /**
   * 会造成堆溢出
   */
  @Test
  public void testHeapOverflow1() {

    List<byte[]> list = new ArrayList<>();
    for (; ; ) {
      list.add(new byte[1024 * 1024 * 5]);
    }
  }

  /**
   * 不会造成堆溢出
   */
  @Test
  public void testHeapOverflow2() {
    for (; ; ) {
      byte[] b = new byte[1024 * 1024 * 5];
    }
  }

  /**
   * 人工主动去调用系统垃圾回收方法
   */
  @Test
  public void testHeapOverflow3() {
    for (; ; ) {
      byte[] b = new byte[1024 * 1024 * 5];
      System.gc();
    }
  }

}
