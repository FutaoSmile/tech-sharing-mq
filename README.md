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
- [publisherReturns参数在spring-boot-starter-amqp中的作用](https://mp.weixin.qq.com/s?__biz=MzI4NjQyMDkyNg==&mid=2247484712&idx=1&sn=fbf43dff40401ed04d27682ed5f3044a&chksm=ebdc7ff9dcabf6efd9283045a6c73c76b06911866c2e97c73cc0c60ddede7997b03c53ae5eff&token=775738946&lang=zh_CN#rd)
- [RabbitMQ消息路由失败的处理方案(回调与备份交换机AE) ](https://mp.weixin.qq.com/s?__biz=MzI4NjQyMDkyNg==&mid=2247484811&idx=1&sn=e45ccad2f55227d58326ac5ec100d2aa&chksm=ebdc7f5adcabf64cd98d594db2aed91ec6810045aab6e42b7f2c65040dfb94e9421ea0a96729&token=2064931214&lang=zh_CN#rd)