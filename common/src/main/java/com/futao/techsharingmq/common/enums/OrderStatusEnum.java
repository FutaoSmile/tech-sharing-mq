package com.futao.techsharingmq.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 订单状态
 *
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/2/9
 */
@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    UN_PAY(0, "待支付"),
    PAID(2, "已支付"),
    SHIPPED(3, "已发货"),
    TO_BE_RECEIVED(4, "待收货"),
    FINISHED(5, "已完成");

    private final int status;
    private final String description;
}
