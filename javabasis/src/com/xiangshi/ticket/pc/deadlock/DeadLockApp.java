package com.xiangshi.ticket.pc.deadlock;

import java.util.LinkedList;

class Producer extends Thread {
  private static int index;
  private String name;
  private TicketPool ticketPool;

  public Producer(String name, TicketPool ticketPool) {
    super(name);
    this.index = 0;
    this.name = name;
    this.ticketPool = ticketPool;
  }

  @Override
  public void run() {
    super.run();
    while (true) {
      ticketPool.addTicket(index++);
    }
  }
}

class Consumer extends Thread {
  private String name;
  private TicketPool ticketPool;

  public Consumer(String name, TicketPool ticketPool) {
    super(name);
    this.name = name;
    this.ticketPool = ticketPool;
  }

  @Override
  public void run() {
    super.run();
    while (true) {
      ticketPool.removeTicket();
    }
  }

}

// 将形成死锁
// 解决方案1: notify() 改成 notifyAll()
// 解决方案2: try语句块 里的wait 加上限制时间
class TicketPool {
  private int maxSize;
  private LinkedList<Integer> tickets;

  public TicketPool(int maxSize, LinkedList<Integer> tickets) {
    this.maxSize = maxSize;
    this.tickets = tickets;
  }

  public synchronized void addTicket(int i) {
    try {
      String name = Thread.currentThread().getName();
      while (tickets.size() >= maxSize) {
        System.out.println(name + " into wait status");
        wait();
      }

      tickets.add(i);
      System.out.println(name + " add " + i);
      System.out.println(name + " will notify");
      notify();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized void removeTicket() {
    try {
      String name = Thread.currentThread().getName();
      while (tickets.size() == 0) {
        System.out.println(name + " into wait status");
        wait();
      }

      int ticketNo = tickets.removeFirst();
      System.out.println(name + " remove " + ticketNo);
      System.out.println(name + " will notify");
      notify();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

// 将形成死锁
public class DeadLockApp {
  public static void main(String[] args) {
    TicketPool tp = new TicketPool(1, new LinkedList<Integer>());
    Producer p1 = new Producer("p1", tp);
    Consumer c1 = new Consumer("c1", tp);
    Consumer c2 = new Consumer("c2", tp);

    p1.start();
    c1.start();
    c2.start();
  }
}
