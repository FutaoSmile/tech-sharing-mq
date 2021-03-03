package com.futao.techsharingmq.consumer.auto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/3/3
 */
@Slf4j
@Component
public class Producer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

     @PostConstruct
    public void send() {
        log.info("send.");
        rabbitTemplate.convertAndSend("auto-ack-exchange", "", "1111");
    }

}
