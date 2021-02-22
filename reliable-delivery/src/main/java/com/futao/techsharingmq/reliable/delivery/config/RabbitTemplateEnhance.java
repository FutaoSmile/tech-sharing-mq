package com.futao.techsharingmq.reliable.delivery.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * RabbitTemplate-Bean增强
 *
 * @author ft <futao@mysteel.com>
 * @date 2021/2/20
 */
@Slf4j
@Component
public class RabbitTemplateEnhance implements BeanPostProcessor {
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof RabbitTemplate) {
            log.debug("增强 RabbitTemplate");
            RabbitTemplate rabbitTemplate = (RabbitTemplate) bean;
            // return回调函数
            rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
                @Override
                public void returnedMessage(ReturnedMessage returnedMessage) {
                    log.error("消息被退回:{}", returnedMessage);
                }
            });

            // 消息发送之前执行
            rabbitTemplate.setBeforePublishPostProcessors(new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    log.info("消息发送之前");
                    return message;
                }
            });

            // 消息接收之后执行
            rabbitTemplate.setAfterReceivePostProcessors(new MessagePostProcessor() {
                @Override
                public Message postProcessMessage(Message message) throws AmqpException {
                    log.info("接收到消息之后");
                    return message;
                }
            });
            return rabbitTemplate;
        }
        return bean;
    }
}
