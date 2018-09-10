package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
@Setter
@Accessors(chain = true)
public class DealerRealTimeMoneyInfo implements Serializable {


    private int id;

    private int specialDealerId;

    @Display("账户")
    private String userName;

    @Display("负责人")
    private String realName;

    @Display("公司")
    private String companyName;

    @Display("期初资金")
    private BigDecimal startMoney = BigDecimal.ZERO;//dtc赋值

    @Display("余额")
    private BigDecimal money = BigDecimal.ZERO;//dtc赋值

    @Display("可用余额")
    private BigDecimal canUseMoney = BigDecimal.ZERO;//dtc赋值

    @Display("转让市值差【对冲】")
    private BigDecimal hedgeCloseProfit = BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单

    @Display("浮动盈亏【对冲】")
    private BigDecimal hedgeFloatProfit = BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单

    @Display("净值")
    private BigDecimal pureMoney = BigDecimal.ZERO;//dtc赋值，最终计算

    @Display("风险率")
    private BigDecimal riskRate = BigDecimal.ZERO;//dtc赋值，最终计算

    @Display("保证金")
    private BigDecimal margin = BigDecimal.ZERO;

    @Display("客户净值")
    private BigDecimal customerPureMoney = BigDecimal.ZERO;//合并

    @Display("转让市值差【客户】")
    private BigDecimal customerCloseProfit = BigDecimal.ZERO;//合并

    @Display("浮动盈亏【客户】")
    private BigDecimal customerFloatProfit = BigDecimal.ZERO;//合并

}
