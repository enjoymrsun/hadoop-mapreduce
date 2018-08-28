package com.xiangshi.network.udp.screenbroadcast;

public class Student {

  public static void main(String[] args) {
    StudentUI ui = new StudentUI();
    (new BroadcastReceiver(ui)).start();
  }
}
