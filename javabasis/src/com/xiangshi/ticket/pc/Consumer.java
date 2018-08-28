package com.xiangshi.ticket.pc;

public class Consumer extends Thread {
  private String name;
  private TicketPool ticketPool;

  public Consumer(String name, TicketPool ticketPool) {
    this.name = name;
    this.ticketPool = ticketPool;
  }

  @Override
  public void run() {
    super.run();

    while (true) {
      int ticketNo = ticketPool.removeTicket();
      System.out.println(name + " remove: " + ticketNo);
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
