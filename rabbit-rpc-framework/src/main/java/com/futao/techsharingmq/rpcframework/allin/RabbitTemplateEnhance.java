package com.futao.techsharingmq.rpcframework.allin;

import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * @author ft <futao@mysteel.com>
 * @date 2021/2/24
 */
@Slf4j
@Component
public class RabbitTemplateEnhance implements BeanPostProcessor {
    public static String QUEUE_NAME;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RabbitTemplate) {
            RabbitTemplate rabbitTemplate = (RabbitTemplate) bean;
            rabbitTemplate.execute(channel -> {
                AMQP.Queue.DeclareOk declareOk = channel.queueDeclare();
                log.info("当前客户端的Queue为:[{}]", declareOk);
                QUEUE_NAME = declareOk.getQueue();
                return declareOk;
            });
        }
        return bean;
    }
}
