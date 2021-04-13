package com.futao.techsharingmq.kafka.basic.controller;

import com.alibaba.fastjson.JSON;
import com.futao.techsharingmq.common.Order;
import com.futao.techsharingmq.kafka.basic.declare.Topics;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.FailureCallback;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.SuccessCallback;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/3/3
 */
@Slf4j
@RequestMapping("/order")
@RestController
public class OrderController {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping
    public void save(@RequestBody Order order) {
        log.info("订单发送到Kafka");
        ListenableFuture<SendResult<String, String>> listenableFuture = kafkaTemplate.send(Topics.ORDER_TOPIC, JSON.toJSONString(order));
        listenableFuture.addCallback(new SuccessCallback<SendResult<String, String>>() {
            @Override
            public void onSuccess(SendResult<String, String> stringOrderSendResult) {
                // 消息发送成功的回调
                log.info("消息投递成功[{}]", stringOrderSendResult);
            }
        }, new FailureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                // 消息发送失败的回调
                log.error("消息投递失败", throwable);
            }
        });
    }

}
