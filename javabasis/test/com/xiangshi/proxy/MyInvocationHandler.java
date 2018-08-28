package com.xiangshi.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class MyInvocationHandler implements InvocationHandler {
  // 可以是各种需要被此handler 代理的类
  // 所以是Object类
  private Object target;

  public MyInvocationHandler(Object o) {
    this.target = o;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    // 你需要增加的功能，这就是那层代理
    System.out.println("Proxy from Java!!");

    // 再调用原始的方法内容
    return method.invoke(target, args);
  }
}
