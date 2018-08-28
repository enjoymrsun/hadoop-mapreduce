package com.xiangshi.ticket.pc;

import java.util.LinkedList;

public class TicketPool {
  private LinkedList<Integer> tickets;
  private int maxSize;

  public TicketPool(LinkedList<Integer> tickets, int maxSize) {
    this.tickets = tickets;
    this.maxSize = maxSize;
  }

  public synchronized int addTicket(int i) {
    while (tickets.size() >= maxSize) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    tickets.add(i);
    notify();

    return i;
  }

  public synchronized int removeTicket() {
    while (tickets.size() == 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    int t = tickets.removeFirst();
    notify();

    return t;
  }

}
