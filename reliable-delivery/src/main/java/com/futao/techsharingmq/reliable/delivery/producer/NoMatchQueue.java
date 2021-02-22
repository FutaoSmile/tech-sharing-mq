package com.futao.techsharingmq.reliable.delivery.producer;

import com.alibaba.fastjson.JSON;
import com.futao.techsharingmq.common.Order;
import com.futao.techsharingmq.common.enums.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/20
 */
@Slf4j
@Component
public class NoMatchQueue {

    public static final String EXCHANGE_NAME = "X_NO_MATCH_QUEUE";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void send() {
        log.info("发送消息");
        Order order = new Order(1, BigDecimal.TEN, OrderStatusEnum.UN_PAY.getStatus());
        Message message = MessageBuilder
                .withBody(JSON.toJSONString(order).getBytes(StandardCharsets.UTF_8))
                .setContentEncoding(StandardCharsets.UTF_8.displayName())
                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
                .build();
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "", message);
    }
}

@Configuration
class ExchangeDeclare {
    @Bean
    public Exchange noMatchQueueExchange() {
        return ExchangeBuilder
                .topicExchange(NoMatchQueue.EXCHANGE_NAME)
                .durable(true)
                .build();
    }
}
