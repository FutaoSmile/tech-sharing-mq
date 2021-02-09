package com.futao.techsharingmq.common;

import com.futao.techsharingmq.common.base.IdTimeEntity;
import com.futao.techsharingmq.common.enums.OrderStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/2/9
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends IdTimeEntity {
    /**
     * 下单用户ID
     */
    private Integer userId;
    /**
     * 订单总价
     */
    private BigDecimal totalPrice;
    /**
     * 订单状态
     *
     * @see OrderStatusEnum
     */
    private int status;
}
