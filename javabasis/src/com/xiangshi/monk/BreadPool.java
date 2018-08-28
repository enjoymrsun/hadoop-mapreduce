package com.xiangshi.monk;

public class BreadPool {

  private int maxForAMonk;
  private int breadsTotal;
  private int breadsRemain;
  private int monksNumber;

  public BreadPool(int maxForAMonk, int breadsRemain, int monksNumber) {
    this.maxForAMonk = maxForAMonk;
    this.breadsTotal = breadsRemain;
    this.breadsRemain = breadsRemain;
    this.monksNumber = monksNumber;
  }

  public synchronized int getBread(Monk monk) throws Exception {
    if (monk.getBreadsEaten() >= maxForAMonk) {
      return 0;
    }

    // 不加1 说明第monksNumber个和尚没有去通知其他人，那么所有和尚都在等待队列里等待
    // 所以需要+1
    if (breadsRemain > breadsTotal - monksNumber + 1) {
      breadsRemain--;
      wait();
      return 1;
    } else if (breadsRemain > 0) {
      breadsRemain--;
      notifyAll();
      return 1;
    }

    return 0;
  }
}
