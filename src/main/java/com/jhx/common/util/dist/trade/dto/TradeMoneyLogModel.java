package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.dist.trade.common.entity.UserMoneyLogType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * desc 用于dt向dtc发送由于交易引起的资金变化
 * author 钱智慧
 * date 2017/11/20 下午10:16
 */
@Getter
@Setter
@Accessors(chain = true)
public class TradeMoneyLogModel {


    private int b2bOrderId;
    private int userId;
    private String userName;
    private BigDecimal money = BigDecimal.ZERO;
    private String note;
    private UserMoneyLogType type;
}
