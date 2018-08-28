package com.xiangshi.ticket.pc;

public class Producer extends Thread {
  private static int index = 0;
  private String name;
  private TicketPool ticketPool;

  public Producer(String name, TicketPool ticketPool) {
    super();
    this.name = name;
    this.ticketPool = ticketPool;
  }

  @Override
  public void run() {
    super.run();

    while (true) {
      int ticketNo = ticketPool.addTicket(index++);
      System.out.println(name + " add: " + ticketNo);
    }
  }
}
