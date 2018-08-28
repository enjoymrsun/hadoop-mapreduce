package com.xiangshi.jvm;

public class CallSelfMain {

  public static int callSelf(int n) {
    System.out.println(n);
    return callSelf(++n);
  }

  public static void main(String[] args) {
    System.out.println("Hello World!");
  }
}
