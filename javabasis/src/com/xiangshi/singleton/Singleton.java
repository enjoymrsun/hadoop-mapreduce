package com.xiangshi.singleton;

public class Singleton {
  private static Singleton singleton = new Singleton();

  public static int count1;
  public static int count2 = 0;

  private Singleton() {
    count1++;
    count2++;
  }

  public static Singleton getInstance() {
    return singleton;
  }

  public static void printCount1(Singleton s) {
    System.out.println("Count1 is: " + s.count1);
  }

  public static void printCount2(Singleton s) {
    System.out.println("Count2 is: " + s.count2);
  }

  public static void main(String[] args) {
    System.out.println("Hello World!");

    Singleton s = Singleton.getInstance();
    printCount1(s);
    printCount2(s);

  }
}
