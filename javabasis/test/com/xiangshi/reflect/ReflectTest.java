package com.xiangshi.reflect;

import com.xiangshi.proxy.IWelcomeService;
import com.xiangshi.proxy.MyInvocationHandler;
import com.xiangshi.proxy.WelcomeServiceImpl;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;

public class ReflectTest {
  /**
   * 测试类
   */
  @Test
  public void testClass() {
    Class clazz = Son.class;
    Method[] ms = clazz.getDeclaredMethods();
    for (Method m : ms) {
      System.out.println(m.getName());
    }
  }

  /**
   * 测试方法
   */
  @Test
  public void testAllMethods() {
    Class clazz = Son.class;
    Method[] ms = clazz.getMethods();
    for (Method m : ms) {
      System.out.println(m.getName());
    }
  }

  @Test
  public void testMethod() throws Exception {
    Class clazz = Son.class;
    Object son = clazz.newInstance();
    Method m = clazz.getDeclaredMethod("getResponse", int.class);
    m.invoke(son, 12);

  }

  @Test
  public void testConstructor() throws Exception {
    Class clazz = Son.class;
    Constructor ctr = clazz.getDeclaredConstructor();
    // 就算构造函数是private 我也能给它改掉，并实例化一个instance
    ctr.setAccessible(true);
    Object s = ctr.newInstance();
    System.out.println(s);
  }

  /**
   * 修饰符
   */
  @Test
  public void testModifier() throws Exception {
    Class clazz = Son.class;
    Method m = clazz.getDeclaredMethod("eat");
    // 得到修饰符总和 (其实是一个数字 每个位都代表着一个修饰)
    int mod = m.getModifiers();

    System.out.println(Modifier.isPrivate(mod));
  }

  /**
   * 测试代理模式
   */
  @Test
  public void testProxy() {
    IWelcomeService w = new WelcomeServiceImpl();
    MyInvocationHandler h = new MyInvocationHandler(w);
    // 创建代理对象 h是handler
    IWelcomeService proxy = (IWelcomeService) Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{IWelcomeService.class}, h);
    proxy.method1();
    proxy.method2();
    proxy.method3();
  }
}
