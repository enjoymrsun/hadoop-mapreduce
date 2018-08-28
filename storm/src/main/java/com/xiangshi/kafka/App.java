package com.xiangshi.kafka;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.kafka.BrokerHosts;
import org.apache.storm.kafka.KafkaSpout;
import org.apache.storm.kafka.SpoutConfig;
import org.apache.storm.kafka.StringScheme;
import org.apache.storm.kafka.ZkHosts;
import org.apache.storm.spout.SchemeAsMultiScheme;
import org.apache.storm.topology.TopologyBuilder;

import java.util.UUID;

/**
 * App
 */
public class App {
  public static void main(String[] args) throws Exception {
    TopologyBuilder builder = new TopologyBuilder();

    //zk连接串
    String zkConnString = "s202:2181";
    //
    BrokerHosts hosts = new ZkHosts(zkConnString);
    //Spout配置
    SpoutConfig spoutConfig = new SpoutConfig(hosts, "test2", "/test2", UUID.randomUUID().toString());
    spoutConfig.scheme = new SchemeAsMultiScheme(new StringScheme());
    KafkaSpout kafkaSpout = new KafkaSpout(spoutConfig);

    builder.setSpout("kafkaspout", kafkaSpout).setNumTasks(2);
    builder.setBolt("split-bolt", new SplitBolt(), 2).shuffleGrouping("kafkaspout").setNumTasks(2);

    Config conf = new Config();
    conf.setNumWorkers(2);
    conf.setDebug(true);

    /**
     * 本地模式storm
     */
    LocalCluster cluster = new LocalCluster();
    cluster.submitTopology("wc", conf, builder.createTopology());
  }
}
