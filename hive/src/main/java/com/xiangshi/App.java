package main.java.com.xiangshi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * 使用jdbc方式连接到hive数据仓库，数据仓库需要开启hiveserver2服务。
 */
public class App {
  public static void main(String[] args) throws Exception {
    Class.forName("org.apache.hive.jdbc.HiveDriver");
    Connection conn = DriverManager.getConnection("jdbc:hive2://192.168.231.201:10000/mydb2");
    Statement st = conn.createStatement();
    ResultSet rs = st.executeQuery("select id , name ,age from t");
    while (rs.next()) {
      System.out.println(rs.getInt(1) + "," + rs.getString(2));
    }
    rs.close();
    st.close();
    conn.close();
  }
}
