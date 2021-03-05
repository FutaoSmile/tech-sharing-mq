package com.futao.techsharingmq.kafka.basic.simple.consumer;

import com.futao.techsharingmq.kafka.basic.simple.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.clients.consumer.internals.PartitionAssignorAdapter;
import org.apache.kafka.common.TopicPartition;

import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/3/5
 */
@Slf4j
public class SimpleConsumer {
    public static void main(String[] args) {
        // 消费者配置
        HashMap<String, Object> configs = new HashMap<>();
        // broker地址
        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.CLUSTER);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        // 消费者ID
        configs.put(ConsumerConfig.CLIENT_ID_CONFIG, "simple-topic-consumer");
        // 消费者群组
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, "simple-consumer-group-1");
        // Partition mapping消费者策略
        configs.put(ConsumerConfig.PARTITION_ASSIGNMENT_STRATEGY_CONFIG, "org.apache.kafka.clients.consumer.RangeAssignor");
        // 自动提交偏移量
        // configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        // 手动提交偏移量
        configs.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        // 提交偏移量的时间间隔
        configs.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "5000");

        KafkaConsumer<String, String> kafkaConsumer = null;
        try {
            kafkaConsumer = new KafkaConsumer<>(configs);
            HashSet<String> topics = new HashSet<>();
            topics.add(Config.TOPIC);
            // 可正则通配符
            // topics.add("text.*");
            // 订阅主题
            kafkaConsumer.subscribe(topics);
            while (true) {
                ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(Duration.ofMillis(100));
                consumerRecords.forEach(consumerRecord -> {
                    log.info("接收到消息:{}", consumerRecords);
                });
                log.info("手动提交偏移量");
                // 同步提交偏移量-在broker响应之前会一直阻塞-会重试
                // kafkaConsumer.commitSync();
                // 异步提交偏移量-不会重试
                kafkaConsumer.commitAsync(new OffsetCommitCallback() {
                    @Override
                    public void onComplete(Map<TopicPartition, OffsetAndMetadata> map, Exception e) {
                        log.info("偏移量提交结果:{}", map, e);
                    }
                });
            }
        } catch (Exception e) {
            log.error("发生异常", e);
        } finally {
            if (kafkaConsumer != null) {
                // 同步提交偏移量，确保提交成功
                kafkaConsumer.commitSync();
                kafkaConsumer.close();
            }
        }
    }
}
