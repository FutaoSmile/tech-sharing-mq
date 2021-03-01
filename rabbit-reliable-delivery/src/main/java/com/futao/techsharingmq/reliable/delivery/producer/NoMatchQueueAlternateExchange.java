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

/**
 * 消息路由失败 - 处理方式: 备份交换机
 *
 * @author ft <futao@mysteel.com>
 * @date 2021/2/22
 */
@Slf4j
@Component
public class NoMatchQueueAlternateExchange {

    /**
     * 正常业务交换机
     */
    public static final String EXCHANGE_NAME = "X_NO_MATCH_QUEUE_ALTERNATE";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     */
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
class NoQueueExchangeDeclare {

    /**
     * 备份交换机
     */
    public static final String X_ALTERNATE = "X_ALTERNATE";

    /**
     * 业务交换机
     *
     * @return
     */
    @Bean
    public Exchange noMatchQueueExchange() {
        return ExchangeBuilder
                .topicExchange(NoMatchQueueAlternateExchange.EXCHANGE_NAME)
                .durable(true)
                // 绑定备份交换机
                .alternate(X_ALTERNATE)
                .build();
    }

    /**
     * 备份队列
     *
     * @return
     */
    @Bean
    public Queue alternateQueue() {
        return QueueBuilder
                .durable("Q_ALTERNATE")
                .build();
    }

    /**
     * 备份交换机
     *
     * @return
     */
    @Bean
    public Exchange alternateExchange() {
        return ExchangeBuilder
                .fanoutExchange(X_ALTERNATE)
                .durable(true)
                .build();
    }

    /**
     * 备份绑定
     *
     * @param alternateExchange
     * @param alternateQueue
     * @return
     */
    @Bean
    public Binding alternateBinding(Exchange alternateExchange, Queue alternateQueue) {
        return BindingBuilder
                .bind(alternateQueue)
                .to(alternateExchange)
                .with("")
                .noargs();
    }

}


