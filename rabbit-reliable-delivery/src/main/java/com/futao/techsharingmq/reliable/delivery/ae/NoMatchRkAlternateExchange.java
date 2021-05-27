//package com.futao.techsharingmq.reliable.delivery.ae;
//
//import com.alibaba.fastjson.JSON;
//import com.futao.techsharingmq.common.Order;
//import com.futao.techsharingmq.common.enums.OrderStatusEnum;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.*;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.PostConstruct;
//import java.math.BigDecimal;
//import java.nio.charset.StandardCharsets;
//import java.util.UUID;
//
///**
// * 消息路由失败(没有) - 处理方式: 备份交换机
// *
// * @author ft <futao@mysteel.com>
// * @date 2021/2/22
// */
//@Slf4j
//@Component
//public class NoMatchRkAlternateExchange {
//
//    /**
//     * 正常业务交换机
//     */
//    public static final String EXCHANGE_NAME = "X_NO_MATCH_RK";
//    /**
//     * 正常业务队列
//     */
//    public static final String QUEUE_NAME = "Q_NO_MATCH_RK";
//    /**
//     * 路由键
//     */
//    public static final String ROUTING_KEY = "log.error";
//
//    /**
//     * 备份交换机
//     */
//    public static final String EXCHANGE_ALTERNATE = "X_AE";
//    /**
//     * 备份队列
//     */
//    public static final String QUEUE_ALTERNATE = "Q_AE";
//
//    @Autowired
//    private RabbitTemplate rabbitTemplate;
//
//    /**
//     * 发送消息
//     */
//    @PostConstruct
//    public void send() {
//        Order order = new Order(1, BigDecimal.TEN, OrderStatusEnum.UN_PAY.getStatus());
//        String correlationId = UUID.randomUUID().toString();
//        Message message = MessageBuilder
//                .withBody(JSON.toJSONString(order).getBytes(StandardCharsets.UTF_8))
//                .setCorrelationId(correlationId)
//                .setContentEncoding(StandardCharsets.UTF_8.displayName())
//                .setContentType(MessageProperties.CONTENT_TYPE_JSON)
//                .build();
//        rabbitTemplate.convertAndSend(EXCHANGE_NAME, "log.info", message, new CorrelationData(correlationId));
//        log.info("发送消息ID:{}", correlationId);
//    }
//}
//
//
//@Configuration
//class NoMatchRkDeclare {
//
//
//    /**
//     * 业务交换机
//     *
//     * @return
//     */
//    @Bean
//    public Exchange noMatchQueueExchange() {
//        return ExchangeBuilder
//                .topicExchange(NoMatchRkAlternateExchange.EXCHANGE_NAME)
//                .durable(true)
//                // 绑定备份交换机
//                .alternate(NoMatchRkAlternateExchange.EXCHANGE_ALTERNATE)
//                .build();
//    }
//
//    /**
//     * 业务队列
//     *
//     * @return
//     */
//    @Bean
//    public Queue queue() {
//        return QueueBuilder
//                .durable(NoMatchRkAlternateExchange.QUEUE_NAME)
//                .build();
//    }
//
//    /**
//     * 业务队列/交换机绑定
//     *
//     * @return
//     */
//    @Bean
//    public Binding binding() {
//        return BindingBuilder
//                .bind(queue())
//                .to(noMatchQueueExchange())
//                .with(NoMatchRkAlternateExchange.ROUTING_KEY)
//                .noargs();
//    }
//
//
//    /**
//     * 备份交换机
//     *
//     * @return
//     */
//    @Bean
//    public Exchange alternateExchange() {
//        return ExchangeBuilder
//                .fanoutExchange(NoMatchRkAlternateExchange.EXCHANGE_ALTERNATE)
//                .durable(true)
//                .build();
//    }
//
//    /**
//     * 备份队列
//     *
//     * @return
//     */
//    @Bean
//    public Queue alternateQueue() {
//        return QueueBuilder
//                .durable(NoMatchRkAlternateExchange.QUEUE_ALTERNATE)
//                .build();
//    }
//
//    /**
//     * 备份绑定
//     *
//     * @param alternateExchange
//     * @param alternateQueue
//     * @return
//     */
//    @Bean
//    public Binding alternateBinding(Exchange alternateExchange, Queue alternateQueue) {
//        return BindingBuilder
//                .bind(alternateQueue)
//                .to(alternateExchange)
//                .with("")
//                .noargs();
//    }
//
//}
//
//
