package com.xiangshi.singleton;

/**
 * 单例模式 -- 饿汉式
 */
public class Dustbin {

  private static Dustbin bin = new Dustbin();

  public static Dustbin getInstance() {
    return bin;
  }

  // 将构造函数私有化，这样就不可以new Dustbin的实例了，只能通过getInstance() 来获得
  private Dustbin() {

  }
}
