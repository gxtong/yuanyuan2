package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * author 钱智慧
 * date 2017/12/25 15:59
 */
@Getter
@Setter
@Accessors(chain = true)
public class SpecialDealerMoneyInfoModel implements Serializable {


    private int id;

    @Display("账户")
    private String userName;

    @Display("公司")
    private String companyName;

    @Display("负责人")
    private String realName;

    @Display("期初资金")
    private BigDecimal startMoney = BigDecimal.ZERO;

    @Display("余额")
    private BigDecimal money = BigDecimal.ZERO;

    @Display("可用余额")
    private BigDecimal canUseMoney = BigDecimal.ZERO;

    @Display("净值")
    private BigDecimal pureMoney = BigDecimal.ZERO;

    @Display("客户净值")
    private BigDecimal allCustomerPureMoney = BigDecimal.ZERO;

    @Display("综合经销商净值")
    private BigDecimal allDealerPureMoney = BigDecimal.ZERO;

    @Display("风险率")
    private BigDecimal riskRate = BigDecimal.ZERO;

    @Display("浮动盈亏【综经对冲】")
    private BigDecimal dealerFloatProfit = BigDecimal.ZERO;

    @Display("转让市值差【综经对冲】")
    private BigDecimal dealerCloseProfit = BigDecimal.ZERO;

}
