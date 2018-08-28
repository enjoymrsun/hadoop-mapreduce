package com.xiangshi.spark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 2018/4/3.
 */
public class ThriftServerClientJava {
  public static void main(String[] args) throws Exception {
    Class.forName("org.apache.hive.jdbc.HiveDriver");
    Connection conn = DriverManager.getConnection("jdbc:hive2://s201:10000");
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("select count(1) from tt where age > 12 ");
    while (rs.next()) {
      int count = rs.getInt(1);
      System.out.println(count);
    }
    rs.close();
  }
}
