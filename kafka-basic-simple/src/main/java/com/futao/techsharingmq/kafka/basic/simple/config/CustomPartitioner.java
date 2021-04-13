package com.futao.techsharingmq.kafka.basic.simple.config;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

/**
 * 自定义分区器
 *
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/3/4
 */
public class CustomPartitioner implements Partitioner {
    @Override
    public int partition(String s, Object o, byte[] bytes, Object o1, byte[] bytes1, Cluster cluster) {
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
