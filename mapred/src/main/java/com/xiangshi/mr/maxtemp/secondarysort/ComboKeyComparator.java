package com.xiangshi.mr.maxtemp.secondarysort;

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * ComboKeyComparator
 */
public class ComboKeyComparator extends WritableComparator {

  protected ComboKeyComparator() {
    super(ComboKey.class, true);
  }

  public int compare(WritableComparable a, WritableComparable b) {
    System.out.println("ComboKeyComparator" + a + "," + b);
    ComboKey k1 = (ComboKey) a;
    ComboKey k2 = (ComboKey) b;
    return k1.compareTo(k2);
  }
}