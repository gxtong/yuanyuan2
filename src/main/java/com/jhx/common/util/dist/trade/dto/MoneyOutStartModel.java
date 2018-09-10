package com.jhx.common.util.dist.trade.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * @date 2017年9月23日 上午10:38:42
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MoneyOutStartModel {


    // Customer或者Worker的Id
    @Min(value = 1)
    @Max(value = Integer.MAX_VALUE , message = "被操作的用户id错误")
    private int userId;

    @Min(value = 1)
    @Max(value = Integer.MAX_VALUE , message = "提现单id错误")
    private int moneyOutId;

    // 是否时人工处理 为true时为人工处理
    private boolean manualHanding;
}
