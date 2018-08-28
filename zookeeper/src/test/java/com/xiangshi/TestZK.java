package com.xiangshi;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;

/**
 *
 */
public class TestZK {

  @Test
  public void ls() throws Exception {
    ZooKeeper zk = new ZooKeeper("s201:2181", 5000, null);
    List<String> list = zk.getChildren("/", null);
    for (String s : list) {
      System.out.println(s);
    }
  }

  @Test
  public void lsAll() throws Exception {
    ls("/");
  }

  /**
   * 列出指定path下的孩子
   */
  public void ls(String path) throws Exception {
    System.out.println(path);
    ZooKeeper zk = new ZooKeeper("s201:2181", 5000, null);
    List<String> list = zk.getChildren(path, null);
    if (list == null || list.isEmpty()) {
      return;
    }
    for (String s : list) {
      //先输出孩子
      if (path.equals("/")) {
        ls(path + s);
      } else {
        ls(path + "/" + s);
      }
    }
  }

  /**
   * 设置数据
   */
  @Test
  public void setData() throws Exception {
    ZooKeeper zk = new ZooKeeper("s201:2181", 5000, null);
    zk.setData("/a", "tomaslee".getBytes(), 0);
  }

  /**
   * 创建临时节点
   */
  @Test
  public void reateEmphoral() throws Exception {
    ZooKeeper zk = new ZooKeeper("s201:2181", 5000, null);
    zk.create("/c/c1", "tom".getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);

    System.out.println("hello");
  }

  @Test
  public void testWatch() throws Exception {
    final ZooKeeper zk = new ZooKeeper("s201:2181", 5000, null);

    Stat st = new Stat();

    Watcher w = null;
    w = new Watcher() {
      //回调
      public void process(WatchedEvent event) {
        try {
          System.out.println("数据改了！！！");
          zk.getData("/a", this, null);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    };

    byte[] data = zk.getData("/a", w, st);

    System.out.println(new String(data));

    while (true) {
      Thread.sleep(1000);
    }
  }


}
