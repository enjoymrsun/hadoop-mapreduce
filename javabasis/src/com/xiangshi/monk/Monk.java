package com.xiangshi.monk;

public class Monk extends Thread {
  private String name;
  private BreadPool breads;
  private int breadsEaten;

  public Monk(String name, BreadPool breads) {
    super(name);
    this.name = name;
    this.breads = breads;
    this.breadsEaten = 0;
  }

  @Override
  public void run() {
    super.run();

    try {
      while (true) {
        int n = breads.getBread(this);
        if (n == 0) {
          break;
        }

        breadsEaten++;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    System.out.println(name + " eat: " + breadsEaten);
  }


  public int getBreadsEaten() {
    return breadsEaten;
  }
}
