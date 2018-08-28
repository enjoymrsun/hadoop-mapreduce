package main.java.com.xiangshi.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 */
@Description(name = "ToChar",
        value = "使用方式如下:toChar()",
        extended = "toChar_xxxx-ext")
public class ToCharUDF extends GenericUDF {

  public ObjectInspector initialize(ObjectInspector[] arguments) throws UDFArgumentException {
    return null;
  }

  public Object evaluate(DeferredObject[] args) throws HiveException {
    //有参数
    if (args != null && args.length != 0) {
      //指定日志对象的格式化串
      if (args.length == 1) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy/MM/dd hh:mm:ss");
        return sdf.format((Date) (args[0].get()));
      }
      //两个参数,Date date,String frt
      else {
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern((String) args[1].get());
        return sdf.format(args[0].get());
      }
    }
    //无参,返回系统时间的格式化串
    else {
      Date date = new Date();
      SimpleDateFormat sdf = new SimpleDateFormat();
      sdf.applyPattern("yyyy/MM/dd hh:mm:ss");
      return sdf.format(date);
    }
  }

  public String getDisplayString(String[] children) {
    return "toChar_xxx";
  }
}
