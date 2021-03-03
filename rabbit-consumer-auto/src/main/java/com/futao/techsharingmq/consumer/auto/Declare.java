package com.futao.techsharingmq.consumer.auto;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/3/3
 */
@Configuration
public class Declare {

    @Bean
    public Exchange autoAckExchange() {
        return ExchangeBuilder
                .fanoutExchange("auto-ack-exchange")
                .durable(true)
                .build();
    }

    @Bean
    public Queue autoAckQueue() {
        return QueueBuilder
                .durable("auto-ack-queue")
                .build();
    }

    @Bean
    public Binding autoAckBinding(Exchange autoAckExchange, Queue autoAckQueue) {
        return BindingBuilder
                .bind(autoAckQueue)
                .to(autoAckExchange)
                .with("")
                .noargs();
    }

}
