package com.xiangshi.proxy;

/**
 * 需要被代理的目标类，包含原始的方法实现
 */
public class WelcomeServiceImpl implements IWelcomeService {
  @Override
  public void method1() {
    System.out.println("This is method1");
  }

  @Override
  public void method2() {
    System.out.println("This is method2");
  }

  @Override
  public void method3() {
    System.out.println("This is method3");
  }
}
