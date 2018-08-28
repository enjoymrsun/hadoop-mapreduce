package com.xiangshi.ticket;

public class Saler extends Thread {

  private String name;
  private TicketPool ticketPool;

  public Saler(String name, TicketPool ticketPool) {
    super();
    this.name = name;
    this.ticketPool = ticketPool;
  }

  @Override
  public void run() {
    while (true) {
      int ticketNo = ticketPool.getTicket();
      if (ticketNo == 0) {
        break;
      }
      System.out.println(name + ": " + ticketNo);
    }
  }
}
