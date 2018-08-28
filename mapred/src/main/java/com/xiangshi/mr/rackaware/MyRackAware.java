package com.xiangshi.mr.rackaware;

import org.apache.hadoop.net.DNSToSwitchMapping;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 机架感知类
 */
public class MyRackAware implements DNSToSwitchMapping {

  public List<String> resolve(List<String> names) {
    List<String> list = new ArrayList<String>();
    try {
      FileWriter fw = new FileWriter("/home/centos/rackaware.txt", true);
      for (String str : names) {
        //输出原来的信息,ip地址(主机名)
        fw.write(str + "\r\n");
        //
        if (str.startsWith("192")) {
          //192.168.231.202
          String ip = str.substring(str.lastIndexOf(".") + 1);
          if (Integer.parseInt(ip) <= 203) {
            list.add("/rack1/" + ip);
          } else {
            list.add("/rack2/" + ip);
          }
        } else if (str.startsWith("s")) {
          String ip = str.substring(1);
          if (Integer.parseInt(ip) <= 203) {
            list.add("/rack1/" + ip);
          } else {
            list.add("/rack2/" + ip);
          }
        }
      }
      fw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return list;
  }

  public void reloadCachedMappings() {

  }

  public void reloadCachedMappings(List<String> names) {
  }
}