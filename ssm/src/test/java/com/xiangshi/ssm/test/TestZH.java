package com.xiangshi.ssm.test;

import com.xiangshi.ssm.util.CharUtil;

import org.junit.Test;

/**
 *
 */
public class TestZH {
  @Test
  public void test1() {
    int nums = 0;
    for (int i = 0x4e00; i <= 0x9fa5; i++) {
      char c = (char) i;
      System.out.print(c);
      nums++;
      if (nums > 51) {
        System.out.println();
        nums = 0;
      }
    }
  }

  @Test
  public void containZH() {
    String str = "abc!!!";
    int count = str.length();
    for (int i = 0; i < count; i++) {
      char c = str.charAt(i);
      int code = (int) c;
      if (code >= 0x4e00 && code <= 0x9fa5) {
        System.out.println("含");
        return;
      }
    }
  }

  @Test
  public void test2() {
    for (; ; ) {
      System.out.println(CharUtil.getName3zh());

    }
  }
}
