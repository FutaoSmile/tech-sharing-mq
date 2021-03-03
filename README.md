## # RabbitMQ技术分享

* 项目维护仓库:
    * https://gitee.com/FutaoSmile/tech-sharing-mq
    * https://github.com/FutaoSmile/tech-sharing-mq

### 一、项目结构

* [common](./common)为公共依赖的一个模板，存放通用的Model。
* [rabbit-basic-concept](./rabbit-basic-concept)为基础概念介绍模块
    * 实现了SpringBoot中集成RabbitMQ
    * 消息生产者发送消息到Exchange(topic)
    * Exchange根据路由规则将消息投递到相应的Queue
    * 消费者绑定队列，消费消息
        - 并且演示了消息的手动签收与自动签收
        - 设置消费者的并发数量
    
* [rabbit-basic-concept-simple](./rabbit-basic-concept-simple) 基础概念，不使用SpringBoot
* [rabbit-reliable-delivery](./rabbit-reliable-delivery) 消息可靠投递
* [rabbit-rpc-framework](./rabbit-rpc-framework) 基于RabbitMQ的RPC框架-demo

### # 其他

- [更多资料](./doc/rabbitmq.md)
- [集群搭建](./doc/集群搭建.md)
- [消息可靠投递到MQ](./rabbit-reliable-delivery/消息可靠投递到MQ.MD)  
- [publisherReturns参数在spring-boot-starter-amqp中的作用](https://mp.weixin.qq.com/s?__biz=MzI4NjQyMDkyNg==&mid=2247484712&idx=1&sn=fbf43dff40401ed04d27682ed5f3044a&chksm=ebdc7ff9dcabf6efd9283045a6c73c76b06911866c2e97c73cc0c60ddede7997b03c53ae5eff&token=775738946&lang=zh_CN#rd)
- [RabbitMQ消息路由失败的处理方案(回调与备份交换机AE) ](https://mp.weixin.qq.com/s?__biz=MzI4NjQyMDkyNg==&mid=2247484811&idx=1&sn=e45ccad2f55227d58326ac5ec100d2aa&chksm=ebdc7f5adcabf64cd98d594db2aed91ec6810045aab6e42b7f2c65040dfb94e9421ea0a96729&token=2064931214&lang=zh_CN#rd)
- [使用RabbitMQ实现未支付订单在30分钟后自动过期](https://mp.weixin.qq.com/s/s8xa_QU8q1W0_Dx4uop-UQ)
- [SpringBoot RabbitMQ实现消息可靠投递](https://mp.weixin.qq.com/s?__biz=MzI4NjQyMDkyNg==&mid=2247483871&idx=1&sn=91272e28cd611462215784315b1dec6a&chksm=ebdc7b0edcabf218f37f3fb5641c95759cb0e3c76527c1ee966bc7470a9ac7414636b06ec225&scene=178&cur_album_id=1355248263913472000#rd)
- [ RabbitMQ死信队列在SpringBoot中的使用 ](https://mp.weixin.qq.com/s?__biz=MzI4NjQyMDkyNg==&mid=2247483884&idx=1&sn=a2be7db538308e3e10296d8a2ea31395&chksm=ebdc7b3ddcabf22bc8efaa64e35b3f2c83cae4e7e1d5ad2eb4c788df8461f67b9480e3486720&scene=178&cur_album_id=1355248263913472000#rd)
- [ SpringBoot如何做到自动帮我们创建RabbitMQ的Queue和Exchange的？ ](https://mp.weixin.qq.com/s?__biz=MzI4NjQyMDkyNg==&mid=2247483913&idx=1&sn=9eb8f64a140450c28205a57d8166ffd7&chksm=ebdc78d8dcabf1ce5ac41b8b9e703d32d42b35f2e30b75f767fc6c95f01637c2b1c95befebe4&scene=178&cur_album_id=1355248263913472000#rd)


### # 书籍
- 《RabbitMQ实战指南》-朱忠华
    - 知识面广，但是不算很深入
    
- 
