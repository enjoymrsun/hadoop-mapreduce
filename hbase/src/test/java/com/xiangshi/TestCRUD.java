package com.xiangshi;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.NamespaceDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.filter.BinaryComparator;
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.DependentColumnFilter;
import org.apache.hadoop.hbase.filter.FamilyFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.KeyOnlyFilter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.filter.QualifierFilter;
import org.apache.hadoop.hbase.filter.RegexStringComparator;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.filter.ValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;

/**
 *
 */
public class TestCRUD {
  @Test
  public void put() throws Exception {
    //创建conf对象
    Configuration conf = HBaseConfiguration.create();
    //通过连接工厂创建连接对象
    Connection conn = ConnectionFactory.createConnection(conf);
    //通过连接查询tableName对象
    TableName tname = TableName.valueOf("ns1:t1");
    //获得table
    Table table = conn.getTable(tname);

    //通过bytes工具类创建字节数组(将字符串)
    byte[] rowid = Bytes.toBytes("row3");

    //创建put对象
    Put put = new Put(rowid);

    byte[] f1 = Bytes.toBytes("f1");
    byte[] id = Bytes.toBytes("id");
    byte[] value = Bytes.toBytes(102);
    put.addColumn(f1, id, value);

    //执行插入
    table.put(put);
  }

  @Test
  public void bigInsert() throws Exception {

    DecimalFormat format = new DecimalFormat();
    format.applyPattern("0000");

    long start = System.currentTimeMillis();
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");
    HTable table = (HTable) conn.getTable(tname);
    //不要自动清理缓冲区
    table.setAutoFlush(false);

    for (int i = 1; i < 10000; i++) {
      Put put = new Put(Bytes.toBytes("row" + format.format(i)));
      //关闭写前日志
      put.setWriteToWAL(false);
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("id"), Bytes.toBytes(i));
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"), Bytes.toBytes("tom" + i));
      put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("age"), Bytes.toBytes(i % 100));
      table.put(put);

      if (i % 2000 == 0) {
        table.flushCommits();
      }
    }
    //
    table.flushCommits();
    System.out.println(System.currentTimeMillis() - start);
  }

  @Test
  public void get() throws Exception {
    //创建conf对象
    Configuration conf = HBaseConfiguration.create();
    //通过连接工厂创建连接对象
    Connection conn = ConnectionFactory.createConnection(conf);
    //通过连接查询tableName对象
    TableName tname = TableName.valueOf("ns1:t1");
    //获得table
    Table table = conn.getTable(tname);

    //通过bytes工具类创建字节数组(将字符串)
    byte[] rowid = Bytes.toBytes("row3");
    Get get = new Get(Bytes.toBytes("row3"));
    Result r = table.get(get);
    byte[] idvalue = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
    System.out.println(Bytes.toInt(idvalue));
  }

  @Test
  public void formatNum() {
    DecimalFormat format = new DecimalFormat();
    format.applyPattern("0000000");
    System.out.println(format.format(8));

  }

  @Test
  public void createNameSpace() throws Exception {
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    Admin admin = conn.getAdmin();
    //创建名字空间描述符
    NamespaceDescriptor nsd = NamespaceDescriptor.create("ns2").build();
    admin.createNamespace(nsd);

    NamespaceDescriptor[] ns = admin.listNamespaceDescriptors();
    for (NamespaceDescriptor n : ns) {
      System.out.println(n.getName());
    }
  }

  @Test
  public void listNameSpaces() throws Exception {
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    Admin admin = conn.getAdmin();

    NamespaceDescriptor[] ns = admin.listNamespaceDescriptors();
    for (NamespaceDescriptor n : ns) {
      System.out.println(n.getName());
    }
  }

  @Test
  public void createTable() throws Exception {
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    Admin admin = conn.getAdmin();
    //创建表名对象
    TableName tableName = TableName.valueOf("ns1:t5");
    //创建表描述符对象
    HTableDescriptor tbl = new HTableDescriptor(tableName);

    //创建列族描述符
    HColumnDescriptor col = new HColumnDescriptor("f1");
    //保留删除的cell
    col.setKeepDeletedCells(true);
    col.setTimeToLive(20);
    tbl.addFamily(col);

    admin.createTable(tbl);
    System.out.println("over");
  }

  @Test
  public void disableTable() throws Exception {
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    Admin admin = conn.getAdmin();
    //禁用表 enable(...) disableTable(...)
    admin.deleteTable(TableName.valueOf("ns2:t2"));
  }


  @Test
  public void deleteData() throws IOException {
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");

    Table table = conn.getTable(tname);
    Delete del = new Delete(Bytes.toBytes("row0001"));
    del.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("id"));
    del.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("name"));
    table.delete(del);
    System.out.println("over");
  }

  /**
   * 删除数据
   */
  @Test
  public void scan() throws IOException {
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");
    Table table = conn.getTable(tname);
    Scan scan = new Scan();
    scan.setStartRow(Bytes.toBytes("row5000"));
    scan.setStopRow(Bytes.toBytes("row8000"));
    ResultScanner rs = table.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      System.out.println(Bytes.toString(name));
    }
  }

  /**
   * 动态遍历
   */
  @Test
  public void scan2() throws IOException {
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");
    Table table = conn.getTable(tname);
    Scan scan = new Scan();
    scan.setStartRow(Bytes.toBytes("row5000"));
    scan.setStopRow(Bytes.toBytes("row8000"));
    ResultScanner rs = table.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      Map<byte[], byte[]> map = r.getFamilyMap(Bytes.toBytes("f1"));
      for (Map.Entry<byte[], byte[]> entrySet : map.entrySet()) {
        String col = Bytes.toString(entrySet.getKey());
        String val = Bytes.toString(entrySet.getValue());
        System.out.print(col + ":" + val + ",");
      }

      System.out.println();
    }
  }

  /**
   * 动态遍历
   */
  @Test
  public void scan3() throws IOException {
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");
    Table table = conn.getTable(tname);
    Scan scan = new Scan();
    scan.setStartRow(Bytes.toBytes("row5000"));
    scan.setStopRow(Bytes.toBytes("row8000"));
    ResultScanner rs = table.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      //得到一行的所有map,key=f1,value=Map<Col,Map<Timestamp,value>>
      NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = r.getMap();
      //
      for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> entry : map.entrySet()) {
        //得到列族
        String f = Bytes.toString(entry.getKey());
        Map<byte[], NavigableMap<Long, byte[]>> colDataMap = entry.getValue();
        for (Map.Entry<byte[], NavigableMap<Long, byte[]>> ets : colDataMap.entrySet()) {
          String c = Bytes.toString(ets.getKey());
          Map<Long, byte[]> tsValueMap = ets.getValue();
          for (Map.Entry<Long, byte[]> e : tsValueMap.entrySet()) {
            Long ts = e.getKey();
            String value = Bytes.toString(e.getValue());
            System.out.print(f + ":" + c + ":" + ts + "=" + value + ",");
          }
        }
      }
      System.out.println();
    }
  }

  /**
   * 按照指定版本数查询
   */
  @Test
  public void getWithVersions() throws IOException {
    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t3");
    Table table = conn.getTable(tname);
    Get get = new Get(Bytes.toBytes("row1"));
    //检索所有版本
    get.setMaxVersions();
    Result r = table.get(get);
    List<Cell> cells = r.getColumnCells(Bytes.toBytes("f1"), Bytes.toBytes("name"));
    for (Cell c : cells) {
      String f = Bytes.toString(c.getFamily());
      String col = Bytes.toString(c.getQualifier());
      long ts = c.getTimestamp();
      String val = Bytes.toString(c.getValue());
      System.out.println(f + "/" + col + "/" + ts + "=" + val);
    }
  }

  /**
   * 删除数据
   */
  @Test
  public void getScanCache() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");
    Scan scan = new Scan();
    scan.setCaching(5000);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    long start = System.currentTimeMillis();
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      System.out.println(r.getColumnLatestCell(Bytes.toBytes("f1"), Bytes.toBytes("name")));
    }
    System.out.println(System.currentTimeMillis() - start);
  }

  /**
   * 测试缓存和批处理
   */
  @Test
  public void testBatchAndCaching() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();
    scan.setCaching(2);
    scan.setBatch(4);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      System.out.println("========================================");
      //得到一行的所有map,key=f1,value=Map<Col,Map<Timestamp,value>>
      NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map = r.getMap();
      //
      for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> entry : map.entrySet()) {
        //得到列族
        String f = Bytes.toString(entry.getKey());
        Map<byte[], NavigableMap<Long, byte[]>> colDataMap = entry.getValue();
        for (Map.Entry<byte[], NavigableMap<Long, byte[]>> ets : colDataMap.entrySet()) {
          String c = Bytes.toString(ets.getKey());
          Map<Long, byte[]> tsValueMap = ets.getValue();
          for (Map.Entry<Long, byte[]> e : tsValueMap.entrySet()) {
            Long ts = e.getKey();
            String value = Bytes.toString(e.getValue());
            System.out.print(f + "/" + c + "/" + ts + "=" + value + ",");
          }
        }
      }
      System.out.println();
    }
  }

  /**
   * 测试RowFilter过滤器
   */
  @Test
  public void testRowFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");
    Scan scan = new Scan();
    RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes("row0100")));
    scan.setFilter(rowFilter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      System.out.println(Bytes.toString(r.getRow()));
    }
  }

  /**
   * 测试FamilyFilter过滤器
   */
  @Test
  public void testFamilyFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();
    FamilyFilter filter = new FamilyFilter(CompareFilter.CompareOp.LESS, new BinaryComparator(Bytes.toBytes("f2")));
    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      System.out.println(f1id + " : " + f2id);
    }
  }

  /**
   * 测试QualifierFilter(列过滤器)
   */
  @Test
  public void testColFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();
    QualifierFilter colfilter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("id")));
    scan.setFilter(colfilter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + f2name);
    }
  }


  /**
   * 测试ValueFilter(值过滤器) 过滤value的值，含有指定的字符子串
   */
  @Test
  public void testValueFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();
    ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("to"));
    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }

  /**
   * 依赖列过滤器
   */
  @Test
  public void testDepFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();
    DependentColumnFilter filter = new DependentColumnFilter(Bytes.toBytes("f2"),
            Bytes.toBytes("name"),
            false,
            CompareFilter.CompareOp.EQUAL,
            new BinaryComparator(Bytes.toBytes("tom2.2"))
    );

    //ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("to"));
    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }

  /**
   * 单列值value过滤，对列上的value进行过滤，不符合整行删除。
   */
  @Test
  public void testSingleColumValueFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();
    SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("f2"),
            Bytes.toBytes("name"),
            CompareFilter.CompareOp.NOT_EQUAL,
            new BinaryComparator(Bytes.toBytes("tom2.2")));

    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }

  /**
   * 单列值排除过滤器,去掉过滤使用的列,对列的值进行过滤
   */
  @Test
  public void testSingleColumValueExcludeFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();
    SingleColumnValueExcludeFilter filter = new SingleColumnValueExcludeFilter(Bytes.toBytes("f2"),
            Bytes.toBytes("name"),
            CompareFilter.CompareOp.EQUAL,
            new BinaryComparator(Bytes.toBytes("tom2.2")));

    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }


  /**
   * 前缀过滤,是rowkey过滤. where rowkey like 'row22%'
   */
  @Test
  public void testPrefixFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");
    Scan scan = new Scan();
    PrefixFilter filter = new PrefixFilter(Bytes.toBytes("row222"));

    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }


  /**
   * 分页过滤,是rowkey过滤,在region上扫描时，对每次page设置的大小。 返回到到client，设计到每个Region结果的合并。
   */
  @Test
  public void testPageFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");
    Scan scan = new Scan();

    PageFilter filter = new PageFilter(10);

    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }

  /**
   * keyOnly过滤器，只提取key,丢弃value.
   */
  @Test
  public void testKeyOnlyFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t1");
    Scan scan = new Scan();

    KeyOnlyFilter filter = new KeyOnlyFilter();

    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }


  /**
   * ColumnPageFilter,列分页过滤器，过滤指定范围列， select ,,a,b from ns1:t7
   */
  @Test
  public void testColumnPageFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();

    ColumnPaginationFilter filter = new ColumnPaginationFilter(2, 2);

    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }


  /**
   *
   */
  @Test
  public void testLike() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();

    ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL,
            new RegexStringComparator("^tom2")
    );

    scan.setFilter(filter);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }

  @Test
  public void testComboFilter() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t7");
    Scan scan = new Scan();

    //where ... f2:age <= 13
    SingleColumnValueFilter ftl = new SingleColumnValueFilter(
            Bytes.toBytes("f2"),
            Bytes.toBytes("age"),
            CompareFilter.CompareOp.LESS_OR_EQUAL,
            new BinaryComparator(Bytes.toBytes("13"))
    );

    //where ... f2:name like %t
    SingleColumnValueFilter ftr = new SingleColumnValueFilter(
            Bytes.toBytes("f2"),
            Bytes.toBytes("name"),
            CompareFilter.CompareOp.EQUAL,
            new RegexStringComparator("^t")
    );
    //ft
    FilterList ft = new FilterList(FilterList.Operator.MUST_PASS_ALL);
    ft.addFilter(ftl);
    ft.addFilter(ftr);

    //where ... f2:age > 13
    SingleColumnValueFilter fbl = new SingleColumnValueFilter(
            Bytes.toBytes("f2"),
            Bytes.toBytes("age"),
            CompareFilter.CompareOp.GREATER,
            new BinaryComparator(Bytes.toBytes("13"))
    );

    //where ... f2:name like %t
    SingleColumnValueFilter fbr = new SingleColumnValueFilter(
            Bytes.toBytes("f2"),
            Bytes.toBytes("name"),
            CompareFilter.CompareOp.EQUAL,
            new RegexStringComparator("t$")
    );
    //ft
    FilterList fb = new FilterList(FilterList.Operator.MUST_PASS_ALL);
    fb.addFilter(fbl);
    fb.addFilter(fbr);


    FilterList fall = new FilterList(FilterList.Operator.MUST_PASS_ONE);
    fall.addFilter(ft);
    fall.addFilter(fb);

    scan.setFilter(fall);
    Table t = conn.getTable(tname);
    ResultScanner rs = t.getScanner(scan);
    Iterator<Result> it = rs.iterator();
    while (it.hasNext()) {
      Result r = it.next();
      byte[] f1id = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("id"));
      byte[] f2id = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("id"));
      byte[] f1name = r.getValue(Bytes.toBytes("f1"), Bytes.toBytes("name"));
      byte[] f2name = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("name"));
      System.out.println(f1id + " : " + f2id + " : " + Bytes.toString(f1name) + " : " + Bytes.toString(f2name));
    }
  }

  /**
   * 测试计数器
   */
  @Test
  public void testIncr() throws IOException {

    Configuration conf = HBaseConfiguration.create();
    Connection conn = ConnectionFactory.createConnection(conf);
    TableName tname = TableName.valueOf("ns1:t8");
    Table t = conn.getTable(tname);
    Increment incr = new Increment(Bytes.toBytes("row1"));
    incr.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("daily"), 1);
    incr.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("weekly"), 10);
    incr.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("monthly"), 100);
    t.increment(incr);

  }

}






















