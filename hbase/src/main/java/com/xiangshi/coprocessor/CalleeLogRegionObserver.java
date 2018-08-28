package com.xiangshi.coprocessor;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

/**
 * 自定义区域观察者
 */
public class CalleeLogRegionObserver extends BaseRegionObserver {

  public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
    FileWriter fw = new FileWriter("/home/centos/kkk.txt", true);

    super.postPut(e, put, edit, durability);
    //
    String tableName0 = TableName.valueOf("ns1:calllogs").getNameAsString();
    //得到当前的TableName对象
    String tableName1 = e.getEnvironment().getRegion().getRegionInfo().getTable().getNameAsString();
    fw.write(tableName1 + "\r\n");
    if (!tableName0.equals(tableName1)) {
      return;
    }

    //得到主叫的rowkey
    //xx , callerid , time ,  direction, calleid  ,duration
    //被叫:calleid,time,

    String rowkey = Bytes.toString(put.getRow());
    String[] arr = rowkey.split(",");
    if (arr[3].equals("1")) {
      return;
    }


    String hash = Util.getRegNo(arr[4], arr[2]);
    //hash

    String newRowKey = hash + "," + arr[4] + "," + arr[2] + ",1," + arr[1] + "," + arr[5];
    Put newPut = new Put(Bytes.toBytes(newRowKey));
    newPut.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("dummy"), Bytes.toBytes("no"));
    TableName tn = TableName.valueOf("ns1:calllogs");
    Table t = e.getEnvironment().getTable(tn);
    //
    fw.write(t.getName().getNameAsString() + "\r\n");
    t.put(newPut);
    fw.close();
  }

  public void preGetOp(ObserverContext<RegionCoprocessorEnvironment> e, Get get, List<Cell> results) throws IOException {
    super.preGetOp(e, get, results);

  }
}
