## # 消息可靠投递到RabbitMQ

### # 消息投递到MQ的过程:

1. 生产者推送消息到MQ-exchange.
2. MQ-server根据消息的routingKey将消息路由到queue.
3. 消息保存到MQ(内存OR硬盘).

这个过程中可能产生的问题及解决方案:

* 消息投递到MQ失败
    * 原因：
        * 如网络闪断导致消息没有被正确地发送到MQ-server
        * MQ-server服务异常
    * 解决方案:
        * 开启消息publisherConfirm，由生产者确认每条消息是否投递成功.
        * 导致新的问题:
            * 消息投递到MQ成功但是MQ回调通知生产者失败
                * 生产者重发消息-那么消费者需要能够处理重复消息
    * 注意：
        * 这类消息应该被再次投递到MQ，所以需要自己实现重试机制
            * 可参考[SpringBoot RabbitMQ实现消息可靠投递](https://mp.weixin.qq.com/s/aC6PQyGAxjbGNAJCrA9p-Q)

* 消息成功到达MQ-server之后，可能路由失败
    * 原因：
        * 无queue与exchange绑定
        * 根据消息的routingKey没有合适的queue来转发消息
    * 解决方案：
        * 备份交换机, AlternateExchange
            * 路由失败的消息将路由到备份交换机
        * ReturnCallBack，路由失败回调
    * 注意：
        * 发生这类问题需要检查exchange和queue的绑定和routingKey关系是否设置正确

* 为了尽量保证消息在MQ-server不丢失，需要保证消息持久化到硬盘，持久化需要确保：
    * exchange开启了持久化
    * queue开启了持久化
    * message开启了持久化
    

### # CodeDemo:
* 生产者确认publisherConfirm:
    * rabbitTemplate
    ```java
    // 生产者确认
    rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
        @Override
        public void confirm(CorrelationData correlationData, boolean ack, String cause) {
            String correlationDataId = correlationData.getId();
            if (ack) {
                log.info("消息:[{}]投递成功", correlationDataId);
            } else {
                log.error("消息[{}]投递失败,cause:[{}]", correlationDataId, cause);
            }
        }
    });
    ```
    * channel
    ```java
    channel.addConfirmListener(new ConfirmListener() {
        @Override
        public void handleAck(long deliveryTag, boolean multiple) throws IOException {
            log.info("消息[{}]投递成功", deliveryTag);
        }
    
        @Override
        public void handleNack(long deliveryTag, boolean multiple) throws IOException {
            log.info("消息[{}]投递成功", deliveryTag);
        }
    });
    ```
  
* AE交换机
```java
@Bean
public Exchange noMatchQueueExchange() {
    return ExchangeBuilder
            .topicExchange(NoMatchQueueAlternateExchange.EXCHANGE_NAME)
            .durable(true)
            // 绑定备份交换机
            //"alternate-exchange", exchange
            .alternate(X_ALTERNATE)
            .build();
}
```

* 路由失败回调:
    * rabbitTemplate
    ```java
    // return回调函数
    rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
        @Override
        public void returnedMessage(ReturnedMessage returnedMessage) {
            log.error("消息被退回:{}", returnedMessage);
        }
    });
    ```
    * channel
    ```java
    // 监听被return的消息
    channel.addReturnListener(new ReturnCallback() {
        @Override
        public void handle(Return returnMessage) {
            log.info("消息被退回:{}", returnMessage);
        }
    });
    ```
