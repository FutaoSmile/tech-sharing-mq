package com.futao.techsharingmq.reliable.delivery.complete.service;

import com.futao.techsharingmq.reliable.delivery.complete.config.RetryProperties;
import com.futao.techsharingmq.reliable.delivery.complete.entity.MessageRecordEntity;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/5/27
 */
@Service
public class MessageRecordService {

    @Autowired
    private RetryProperties retryProperties;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void save(MessageRecordEntity messageRecord) {
        MessageRecordEntity recordEntity = messageRecord.selectById();
        if (recordEntity == null) {
            // 新增
            messageRecord.insert();
        } else {
            int maxTimes = retryProperties.getMaxTimes();
            if (recordEntity.getRetryTimes() >= maxTimes) {
                // 超出或等于重试次数

            } else {
                //重试
                rabbitTemplate.convertAndSend(
                        recordEntity.getExchangeName(),
                        recordEntity.getRoutingKey(),
                        recordEntity.getMsgBody(),
                        new CorrelationData(recordEntity.getId()));

            }
        }

    }
}
