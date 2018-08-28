package com.xiangshi.ticket;

public class TicketPool {
  private int ticketAmount;

  public TicketPool(int ticketAmount) {
    this.ticketAmount = ticketAmount;
  }

  // 同步
  // 加入 synchronized 解决2个售票员重复卖票的问题
  public synchronized int getTicket() {
    int ticketNo = ticketAmount;
    if (ticketNo == 0) {
      return 0;
    }

    ticketAmount--;

    return ticketNo;
  }
}
