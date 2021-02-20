package com.futao.techsharingmq.basic.concept.consumer;

import com.alibaba.fastjson.JSON;
import com.futao.techsharingmq.common.Order;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 订单消费着
 *
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/2/9
 */
@Slf4j
@Component
public class OrderConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 小定消费者
     * 手动签收，并发为1
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = "pre-order-queue", ackMode = "AUTO")
    public void preOrderConsumer(Message message, Order order) {
        // log.info("接收到小定:{}", JSON.toJSONString(message, true));
        log.info("接收到小定:{}", JSON.toJSONString(order, true));
    }

    /**
     * 大定消费者
     * 手动签收, 并发为4
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = "official-order-queue", ackMode = "MANUAL", concurrency = "4")
    public void officialOrderConsumer(Message message, Order order, Channel channel) throws IOException {
        log.info("接收到大定:{}", JSON.toJSONString(order, true));
        // 进行手动签收
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        log.info("deliveryTag:{}", deliveryTag);
        channel.basicAck(deliveryTag, false);
    }

    /**
     * 所有订单消费者
     * 自动, 并发为4
     *
     * @param message
     */
    @RabbitHandler
    @RabbitListener(queues = "all-order-queue", ackMode = "AUTO", concurrency = "4")
    public void allOrderConsumer(Message message, Order order) {
        log.info("接收到订单:{}", JSON.toJSONString(order, true));
    }
}