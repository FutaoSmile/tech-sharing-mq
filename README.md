## # RabbitMQ技术分享

* 项目维护仓库:
    * https://gitee.com/FutaoSmile/tech-sharing-mq
    * https://github.com/FutaoSmile/tech-sharing-mq

### 一、项目结构

* [common](./common)为公共依赖的一个模板，存放通用的Model。
* [basic-concept](./basic-concept)为基础概念介绍模块
    * 实现了SpringBoot中集成RabbitMQ
    * 消息生产者发送消息到Exchange(topic)
    * Exchange根据路由规则将消息投递到相应的Queue
    * 消费者绑定队列，消费消息
        - 并且演示了消息的手动签收与自动签收
        - 设置消费者的并发数量
    
* [basic-concept-simple](./basic-concept-simple) 基础概念，不使用SpringBoot
* [reliable-delivery](./reliable-delivery) 消息可靠投递

### # 其他

- [更多资料](./doc/rabbitmq.md)