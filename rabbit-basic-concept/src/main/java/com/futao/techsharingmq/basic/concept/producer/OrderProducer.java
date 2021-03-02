package com.futao.techsharingmq.basic.concept.producer;

import com.futao.techsharingmq.common.Order;
import com.futao.techsharingmq.common.enums.OrderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.stream.IntStream;

/**
 * 订单生产者
 *
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/2/9
 */
@Slf4j
@Component
public class OrderProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Exchange orderExchangeTopic;

//    @PostConstruct
    public void send() {
        log.info("开始推送订单消息");
        int msgCount = 1_00;
        // 小定
        IntStream.rangeClosed(1, msgCount)
                .forEach(i -> {
                    Order order = new Order(i, new BigDecimal(i), OrderStatusEnum.UN_PAY.getStatus());
                    rabbitTemplate.convertAndSend(orderExchangeTopic.getName(), "order.pre", order);
                });

        // 大定
        IntStream.rangeClosed(1, msgCount)
                .forEach(i -> {
                    Order order = new Order(i, new BigDecimal(i), OrderStatusEnum.UN_PAY.getStatus());
                    rabbitTemplate.convertAndSend(orderExchangeTopic.getName(), "order.official", order);
                });

        // 路由键是xxx
        IntStream.rangeClosed(1, msgCount)
                .forEach(i -> {
                    Order order = new Order(i, new BigDecimal(i), OrderStatusEnum.UN_PAY.getStatus());
                    rabbitTemplate.convertAndSend(orderExchangeTopic.getName(), "order.xxx", order);
                });
        log.info("订单消息推送完成");
    }
}
