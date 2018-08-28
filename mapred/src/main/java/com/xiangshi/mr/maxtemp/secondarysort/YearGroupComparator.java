package com.xiangshi.mr.maxtemp.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * 按照年份进行分组对比器实现
 */
public class YearGroupComparator extends WritableComparator {

  protected YearGroupComparator() {
    super(ComboKey.class, true);
  }

  public int compare(WritableComparable a, WritableComparable b) {
    System.out.println("YearGroupComparator" + a + "," + b);
    ComboKey k1 = (ComboKey) a;
    ComboKey k2 = (ComboKey) b;
    return k1.getYear() - k2.getYear();
  }
}
