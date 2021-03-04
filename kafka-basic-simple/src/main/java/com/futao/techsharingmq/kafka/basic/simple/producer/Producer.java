package com.futao.techsharingmq.kafka.basic.simple.producer;

import com.futao.techsharingmq.kafka.basic.simple.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.*;

import java.util.HashMap;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

/**
 * 消息生产者
 *
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/3/4
 */
@Slf4j
public class Producer {
    public static void main(String[] args) throws ExecutionException, InterruptedException, BrokenBarrierException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        // 配置
        HashMap<String, Object> configs = new HashMap<>();
        // broker地址
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, Config.CLUSTER);
        // key的序列化器
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        // value的序列化器
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        // 多少个分区副本接收到消息就认为消息投递到Kafka成功。0=不等待，1=首领节点接收到消息，all=所有节点
        configs.put(ProducerConfig.ACKS_CONFIG, "1");
        // 消息重试次数
        configs.put(ProducerConfig.RETRIES_CONFIG, "3");
        // 消息重试时间间隔
        configs.put(ProducerConfig.RETRY_BACKOFF_MS_CONFIG, "100");
        // 将批次消息发送到kafka之前等待的时间，或者批次满了就发送
        configs.put(ProducerConfig.LINGER_MS_CONFIG, 100);
        // 客户端-线程名也会设置成一样的
        configs.put(ProducerConfig.CLIENT_ID_CONFIG, "order-producer");
        // 在接收到服务器响应之前可以发送多少个消息
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, 1);

        // Producer
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(configs);

        // 构造消息
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(Config.TOPIC, "我是内容");
        // 同步等待结果
//        kafkaProducer.send(producerRecord).get();
        // 异步通知结果
        kafkaProducer.send(producerRecord, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                log.info("消息发送结果:{}", recordMetadata, e);
                countDownLatch.countDown();
            }
        });
        log.info("等待结果");
        countDownLatch.await();
    }

}
