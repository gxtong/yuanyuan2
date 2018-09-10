package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * author 钱智慧
 * date 2017/12/13 18:34
 */
@Getter
@Setter
@Accessors(chain = true)
public class DtcDtModel implements Serializable{


    private int dealerOrSpecialDealerId;
    private BigDecimal riskRate = BigDecimal.ZERO;
    private BigDecimal baseMoney = BigDecimal.ZERO;
    private BaseMoneyType baseMoneyType;
    private boolean frozen;

    public DtcDtModel(int dealerOrSpecialDealerId, BigDecimal riskRate,boolean frozen, BigDecimal baseMoney, BaseMoneyType baseMoneyType) {
        this.dealerOrSpecialDealerId = dealerOrSpecialDealerId;
        this.riskRate = riskRate;
        this.frozen=frozen;
        this.baseMoney = baseMoney;
        this.baseMoneyType=baseMoneyType;
    }
}
