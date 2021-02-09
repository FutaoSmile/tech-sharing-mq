package com.futao.techsharingmq.common.base;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 基类
 *
 * @author futao <1185172056@qq.com> <https://github.com/FutaoSmile>
 * @date 2021/2/9
 */
@Getter
@Setter
public class IdTimeEntity implements Serializable {
    private Integer id;
    private Integer createBy;
    private LocalDateTime createDateTime;
    private Integer updateBy;
    private LocalDateTime updateDateTime;
}
