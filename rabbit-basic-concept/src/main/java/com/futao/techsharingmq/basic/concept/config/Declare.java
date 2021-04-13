package com.futao.techsharingmq.basic.concept.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 队列，交换机，绑定关系定义
 *
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/2/9
 */
@Configuration
public class Declare {

    /**
     * 定义持久化topic交换机
     *
     * @return
     */
    @Bean
    public Exchange orderExchangeTopic() {
        return ExchangeBuilder
                .topicExchange("order-exchange-topic")
                .durable(true)
                .build();
    }

    /**
     * 定义预订订单持久化队列
     *
     * @return
     */
    @Bean
    public Queue preOrderQueue() {
        return QueueBuilder
                .durable("pre-order-queue")
                .build();
    }

    /**
     * 定义正式订单持久化队列
     *
     * @return
     */
    @Bean
    public Queue officialOrderQueue() {
        return QueueBuilder
                .durable("official-order-queue")
                .build();
    }

    /**
     * 定义所有订单持久化队列
     *
     * @return
     */
    @Bean
    public Queue allOrderQueue() {
        return QueueBuilder
                .durable("all-order-queue")
                .build();
    }


    /**
     * preOrder队列与交换机进行绑定
     *
     * @param preOrderQueue
     * @param orderExchangeFanout
     * @return
     */
    @Bean
    public Binding preOrderBinding(Queue preOrderQueue, Exchange orderExchangeFanout) {
        return BindingBuilder
                .bind(preOrderQueue)
                .to(orderExchangeFanout)
                // 路由键
                .with("order.pre")
                .noargs();
    }

    /**
     * official队列与交换机进行绑定
     *
     * @param officialOrderQueue
     * @param orderExchangeFanout
     * @return
     */
    @Bean
    public Binding officialOrderBinding(Queue officialOrderQueue, Exchange orderExchangeFanout) {
        return BindingBuilder
                .bind(officialOrderQueue)
                .to(orderExchangeFanout)
                // 路由键
                .with("order.official")
                .noargs();
    }

    /**
     * 所有订单队列与交换机进行绑定
     *
     * @param allOrderQueue
     * @param orderExchangeFanout
     * @return
     */
    @Bean
    public Binding allOrderBinding(Queue allOrderQueue, Exchange orderExchangeFanout) {
        return BindingBuilder
                .bind(allOrderQueue)
                .to(orderExchangeFanout)
                // 路由键
                .with("order.*")
                .noargs();
    }


}
