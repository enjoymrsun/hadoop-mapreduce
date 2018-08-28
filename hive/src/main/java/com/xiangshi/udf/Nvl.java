package main.java.com.xiangshi.udf;

import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentLengthException;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDFUtils;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;

/**
 * 自定义null值处理函数
 */
public class Nvl extends GenericUDF {
  private GenericUDFUtils.ReturnObjectInspectorResolver returnOIResolver;
  private ObjectInspector[] argumentOIs;

  public ObjectInspector initialize(ObjectInspector[] arguments)
          throws UDFArgumentException {
    argumentOIs = arguments;
    //检查参数个数
    if (arguments.length != 2) {
      throw new UDFArgumentLengthException(
              "The operator 'NVL' accepts 2 arguments.");
    }
    returnOIResolver = new GenericUDFUtils.ReturnObjectInspectorResolver(true);
    //检查参数类型
    if (!(returnOIResolver.update(arguments[0]) && returnOIResolver
            .update(arguments[1]))) {
      throw new UDFArgumentTypeException(2,
              "The 1st and 2nd args of function NLV should have the same type, "
                      + "but they are different: \"" + arguments[0].getTypeName()
                      + "\" and \"" + arguments[1].getTypeName() + "\"");
    }
    return returnOIResolver.get();
  }

  public Object evaluate(DeferredObject[] arguments) throws HiveException {
    Object retVal = returnOIResolver.convertIfNecessary(arguments[0].get(), argumentOIs[0]);
    if (retVal == null) {
      retVal = returnOIResolver.convertIfNecessary(arguments[1].get(),
              argumentOIs[1]);
    }
    return retVal;
  }

  public String getDisplayString(String[] children) {
    StringBuilder sb = new StringBuilder();
    sb.append("if ");
    sb.append(children[0]);
    sb.append(" is null ");
    sb.append("returns");
    sb.append(children[1]);
    return sb.toString();
  }
}