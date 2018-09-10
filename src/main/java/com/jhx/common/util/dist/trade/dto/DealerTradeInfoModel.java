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
public class DealerTradeInfoModel implements Serializable{

    private int dealerId;

    @Display("账户")
    private String dealerUserName;

    @Display("公司")
    private String companyName;

    @Display("负责人")
    private String realName;

    private int productId;

    @Display("商品")
    private String productName;

    @Display("规格")
    private int productCount;

    @Display("买持有量【客户】")
    private BigDecimal customerBuyCount=BigDecimal.ZERO;

    @Display("卖持有量【客户】")
    private BigDecimal customerSellCount=BigDecimal.ZERO;

    @Display("买浮动盈亏【客户】")
    private BigDecimal customerBuyFloatProfit=BigDecimal.ZERO;

    @Display("卖浮动盈亏【客户】")
    private BigDecimal customerSellFloatProfit=BigDecimal.ZERO;

    @Display("买持有量【对冲】")
    private BigDecimal buyCount=BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单

    @Display("卖持有量【对冲】")
    private BigDecimal sellCount=BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单

    @Display("买浮动盈亏【对冲】")
    private BigDecimal buyFloatProfit=BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单

    @Display("卖浮动盈亏【对冲】")
    private BigDecimal sellFloatProfit=BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单
}
