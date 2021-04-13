package com.futao.techsharingmq.kafka.basic.consumer;

import com.alibaba.fastjson.JSON;
import com.futao.techsharingmq.common.Order;
import com.futao.techsharingmq.kafka.basic.declare.Topics;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/3/3
 */
@Slf4j
@Component
public class OrderConsumer {

    @KafkaHandler
    @KafkaListener(topics = Topics.ORDER_TOPIC)
    public void orderConsumer(ConsumerRecord<?, ?> consumerRecord) {
        log.info("接收到消息:[{}]", consumerRecord);
    }

//    @KafkaHandler
//    @KafkaListener(topics = Topics.ORDER_TOPIC)
//    public void orderConsumer2(Order order) {
//        log.info("接收到消息:[{}]", JSON.toJSONString(order));
//    }
}
