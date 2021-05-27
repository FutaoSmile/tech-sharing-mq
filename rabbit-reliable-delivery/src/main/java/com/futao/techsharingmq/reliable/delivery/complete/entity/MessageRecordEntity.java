package com.futao.techsharingmq.reliable.delivery.complete.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 消息投递记录表
 *
 * @author ft
 * @date 2021/5/27
 */
@Getter
@Setter
@TableName("message_record")
public class MessageRecordEntity extends Model<MessageRecordEntity> {
    /**
     * 主键
     */
    @TableId(type = IdType.AUTO)
    private int id;
    /**
     * 消息体
     */
    private String msgBody;
    /**
     * 交换机
     */
    private String exchangeName;
    /**
     * 消息路由键
     */
    private String routingKey;
    /**
     * 状态
     *
     * @see MessageRecordStatusEnum
     */
    private int status;
    /**
     * 重试次数
     */
    private int retryTimes;
    /**
     * 下次重试时间
     */
    private LocalDateTime nextRetryDateTime;
}
