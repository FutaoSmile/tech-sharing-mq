package com.futao.techsharingmq.reliable.delivery.complete.entity;

/**
 * 消息投递状态
 *
 * @author ft
 * @date 2021/5/27
 */
public enum MessageRecordStatusEnum {
    SENDING(0, "投递中"),
    SUCCESS(1, "投递成功"),
    FAIL(2, "投递失败");

    private final int status;
    private final String description;

    MessageRecordStatusEnum(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }
}
