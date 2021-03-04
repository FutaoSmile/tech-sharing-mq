## # Kafka

### # 资源

- 监控
    - https://github.com/didi/Logi-KafkaManager
    - https://github.com/yahoo/CMAK

### # 资料

* 博客文章
    - [SpringBoot集成kafka全面实战](https://mp.weixin.qq.com/s/JvKPbvJ7q0ypqfF3ARBW7Q)

###    # 

* kafka相比于其他MQ，除了支持持久化还支持流式处理

###    # 

- 如果能在最开始接触这门技术时能看到这本书就好了。

### # 阅读笔记

##### 1.2.3 主题和分区

* 消息通过Topic进行分类，每个Topic又被划分为很多个Partition分区,消息被不断地追加到Partition，每个Partition的消息FIFO，但是Topic整体上并不保证FIFO。

##### 1.2.4 生产者与消费者

- 生产者Producer，生产者一般将消息均匀地投递到Topic所有的Partition，但是也可以实现自定义的分区规则，如使用Hash散列的方式，或者根据业务规则将消息投递到不同的Partition。
- 消费者Consumer可以订阅一个或者多个主题，并且根据偏移量来读取消息。
- 偏移量Offset，每个分区的偏移量记录了消息消费的位置，是一个不断递增的整数，并且被存储在ZK或者Kafka上。消费者断线重新连接也不会丢失。
- 消费者群组ConsumerGroup，多个消费者组成的消费者群组，群组保证一个分区只会被某一个消费者读取

##### 1.2.5 broker与集群

- 一个broker为一个Kafka服务器
-
多个broker组成集群。集群中有一个broker充当集群控制器的角色，一个Partition可以被分配给多个broker，主broker负责Producer和Consumer的读写，其他broker会Replication消息进行冗余。
![img.png](img.png)

### 2、安装Kafka

* 依赖:
    * JDK
        * Kafka是使用java开发的
    * Zookeeper
        * Kafka使用Zookeeper来保存broker的元数据和消费者信息
* http://kafka.apache.org/downloads
* 启动:
    1. 启动ZK：
        * `brew services start zookeeper`
        * `bin/zkServer.sh start conf/zoo.cfg`
    1. 启动Kafka：`bin/kafka-server-start.sh config/server.properties`

#### 集群

- 提示: kafka在使用zk的过程中一般都是多个集群连接到同一个ZK集群，为了避免配置被冲突和覆盖，最佳实践是在配置zk连接的时候指定chroot路径:
    - 配置方式: kafka: server.properties，zk连接地址: `zookeeper.connect=localhost:2181/kafka-cluster-demo`

![img_1.png](img_1.png)

### # 3. 生产者

* 发送消息的主要过程
  ![img_2.png](img_2.png)
  
* 如果没有指定ProducerRecord的分区，那么分区器会根据ProducerRecord来计算一个分区，随后该条消息会被添加到一个批次，同一个批次内的消息会被发送到同一个Topic的同一个Partition，有一个线程专门做这个事情。
- 如果消息成功写入Kafka，会返回消息的Topic，Partition和Offset。