package com.futao.techsharingmq.reliable.delivery.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/3/3
 */
@Component
public class Producer {
    @Autowired
    private RabbitTemplate rabbitTemplate;

//    @PostConstruct
//    public void send() {
//        rabbitTemplate.send("");
//    }

}
