package com.futao.techsharingmq.rpcframework.allin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/24
 */
@Slf4j
@Configuration
public class Declare {

    /**
     * RPC交换机
     */
    public static final String RPC_EXCHANGE_NAME = "x-rpc";
    /**
     *
     */
    public static final String RPC_QUEUE_NAME = "q-rpc";


    @Bean
    public Exchange rpcExchange() {
        return ExchangeBuilder
                .fanoutExchange(RPC_EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    @Bean
    public Queue rpcQueue() {
        return QueueBuilder
                .durable(RPC_QUEUE_NAME)
                .build();
    }

    @Bean
    public Binding rpcBinding(Exchange rpcExchange, Queue rpcQueue) {
        return BindingBuilder
                .bind(rpcQueue)
                .to(rpcExchange)
                .with("")
                .noargs();
    }
}
