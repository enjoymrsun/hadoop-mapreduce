package com.xiangshi.reflect;

public class Son extends Father {
  public int property = 8000;

  private Son() {

  }

  public void learnCS() {
    System.out.println("Learning Computer Science!");
  }

  public void getResponse(int key) {
    System.out.println("key is: " + key);
  }

  private void eat() {
    System.out.println("eat burger");
  }

}
