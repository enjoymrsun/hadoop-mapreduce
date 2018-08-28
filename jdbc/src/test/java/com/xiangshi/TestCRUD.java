package com.xiangshi;

import org.junit.Test;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Types;

/**
 * 测试基本操作
 */
public class TestCRUD {
  /**
   * 语句对象，不能防止sql注入，性能较低。
   */
  @Test
  public void testStatement() throws Exception {
    long start = System.currentTimeMillis();
    //创建连接
    String driverClass = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/big4";
    String username = "root";
    String password = "root";
    Class.forName(driverClass);
    Connection conn = DriverManager.getConnection(url, username, password);
    //关闭自动提交
    conn.setAutoCommit(false);

    //创建语句
    Statement st = conn.createStatement();
    for (int i = 0; i < 1000000; i++) {
      String sql = "insert into users(name,age) values('tomas" + i + "'," + (i % 100) + ")";
      st.execute(sql);
    }
    conn.commit();
    st.close();
    conn.close();
    System.out.println(System.currentTimeMillis() - start);
  }

  /**
   * 语句对象，不能防止sql注入，性能较低。
   */
  @Test
  public void testPreparedStatement() throws Exception {
    long start = System.currentTimeMillis();
    //创建连接
    String driverClass = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/big4";
    String username = "root";
    String password = "root";
    Class.forName(driverClass);
    Connection conn = DriverManager.getConnection(url, username, password);
    //关闭自动提交
    conn.setAutoCommit(false);

    //创建语句
    String sql = "insert into users(name,age) values(?,?)";
    PreparedStatement ppst = conn.prepareStatement(sql);
    for (int i = 0; i < 1000000; i++) {
      ppst.setString(1, "tom" + i);
      ppst.setInt(2, i % 100);
      //将sql保存到批次中
      ppst.addBatch();
      if (i % 2000 == 0) {
        //统一执行批次(批量提交)
        ppst.executeBatch();
      }
    }
    ppst.executeBatch();
    conn.commit();
    ppst.close();
    conn.close();
    System.out.println(System.currentTimeMillis() - start);
  }

  /**
   */
  @Test
  public void testCallableStatement() throws Exception {
    long start = System.currentTimeMillis();
    //创建连接
    String driverClass = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/big4";
    String username = "root";
    String password = "root";
    Class.forName(driverClass);
    Connection conn = DriverManager.getConnection(url, username, password);
    //关闭自动提交
    conn.setAutoCommit(false);

    //创建可调用语句，调用存储过程
    CallableStatement cst = conn.prepareCall("{call sp_batchinsert(?)}");
    cst.setInt(1, 1000000);        //绑定参数
    //注册输出参数类型
    cst.execute();
    conn.commit();
    conn.close();
    System.out.println(System.currentTimeMillis() - start);
  }

  /**
   * 通过callableStatment调用mysql的函数
   */
  @Test
  public void testFunction() throws Exception {
    long start = System.currentTimeMillis();
    //创建连接
    String driverClass = "com.mysql.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306/big4";
    String username = "root";
    String password = "root";
    Class.forName(driverClass);
    Connection conn = DriverManager.getConnection(url, username, password);
    //关闭自动提交
    conn.setAutoCommit(false);

    //创建可调用语句，调用存储过程
    CallableStatement cst = conn.prepareCall("{? = call sf_add(?,?)}");
    cst.setInt(2, 100);
    cst.setInt(3, 200);
    cst.registerOutParameter(1, Types.INTEGER);
    //注册输出参数类型
    cst.execute();
    System.out.println(cst.getInt(1));
    conn.commit();
    conn.close();
    System.out.println(System.currentTimeMillis() - start);
  }
}