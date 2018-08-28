package com.xiangshi.demo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 */
public class App {
  public static void main(String[] args) {
    try {
      //创建连接
      String driverClass = "com.mysql.jdbc.Driver";
      String url = "jdbc:mysql://localhost:3306/big4";
      String username = "root";
      String password = "root";
      Class.forName(driverClass);
      Connection conn = DriverManager.getConnection(url, username, password);

      //创建语句
      Statement st = conn.createStatement();
      String sql = "insert into users(name,age) values('tom',12)";
      st.execute(sql);
      st.close();
      conn.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
