package main.java.com.xiangshi.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

/**
 * 自定义hive函数
 */
@Description(name = "myadd",
        value = "add(int a , int b) ==> return a + b ",
        extended = "Example:\n"
                + " add(1,1) ==> 2 \n"
                + " add(1,2,3) ==> 6;")
public class AddUDF extends UDF {

  public int evaluate(int a, int b) {
    return a + b;
  }

  public int evaluate(int a, int b, int c) {
    return a + b + c;
  }
}