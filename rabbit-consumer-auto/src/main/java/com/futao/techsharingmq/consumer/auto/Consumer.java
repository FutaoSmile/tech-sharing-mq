package com.futao.techsharingmq.consumer.auto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/3/3
 */
@Component
@Slf4j
public class Consumer {

    private static volatile int retryTimes = 0;

    @RabbitHandler
    @RabbitListener(queues = "auto-ack-queue")
    public void consumer(Message message) {
        String messageBody = new String(message.getBody(), StandardCharsets.UTF_8);
        retryTimes();
        log.info("接收到消息:[{}]", messageBody);
        int i = 1 / 0;
    }

    public void retryTimes() {
        synchronized (Consumer.class) {
            retryTimes = retryTimes + 1;
            log.info("投递次数:[{}]", retryTimes);
        }
    }
}
