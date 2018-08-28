package com.xiangshi.ticket;

public class TicketApp {
  public static void main(String[] args) {
    TicketPool tp = new TicketPool(50);
    Saler curry = new Saler("curry", tp);
    Saler james = new Saler("james", tp);
    curry.start();
    james.start();
  }
}
