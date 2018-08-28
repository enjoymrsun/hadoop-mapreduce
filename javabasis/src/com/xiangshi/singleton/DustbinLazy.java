package com.xiangshi.singleton;

/**
 * 单例模式 -- 懒汉式
 */
public class DustbinLazy {

  private static DustbinLazy bin = new DustbinLazy();

  private DustbinLazy() {
  }

  public static DustbinLazy getInstance() {
    if (bin == null) {
      synchronized (DustbinLazy.class) {
        if (bin == null) {
          bin = new DustbinLazy();
        }
      }
    }
    return bin;
  }
}
