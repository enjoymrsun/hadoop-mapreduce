package com.xiangshi.monk;


/**
 * 4.和尚吃馒头问题 | a) x个馒头 | b) y个和尚 | c) 每个和尚至少吃一个馒头 | d) 最多不超过z个馒头.
 */
public class MonkApp {
  public static void main(String[] args) {
    int monksNumber = 3;
    BreadPool breads = new BreadPool(5, 10, monksNumber);
    for (int i = 1; i <= monksNumber; i++) {
      new Monk("Monk" + String.format("%02d", i), breads).start();
    }
  }
}
