package com.xiangshi.ticket.pc;

import java.util.LinkedList;

/**
 * 当TicketPool的maxSize为1时，可能发生死锁（代码展示在deadlock包里） 处理方式一个是将notify() 改成 notifyAll() 另一个是将线程放入线程等待序列里加上限制时间，过限定时间后自动去抢cpu资源，不用再进行notify()操作
 */
public class PCApp {

  public static void main(String[] args) {
    TicketPool tp = new TicketPool(new LinkedList<Integer>(), 20);
    Producer p1 = new Producer("p1", tp);
    Producer p2 = new Producer("p2", tp);
    Consumer c1 = new Consumer("c1", tp);
    Consumer c2 = new Consumer("c2", tp);

    p1.start();
    p2.start();
    c1.start();
    c2.start();
  }
}
