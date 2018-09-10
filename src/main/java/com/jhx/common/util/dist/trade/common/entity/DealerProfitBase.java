package com.jhx.common.util.dist.trade.common.entity;

import com.jhx.common.util.db.DbConstant;
import com.jhx.common.util.db.IdEntityBase;
import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * author 钱智慧
 * date 2017/12/12 10:05
 */
@Getter
@Setter
@Accessors(chain = true)
@MappedSuperclass
public class DealerProfitBase extends IdEntityBase {


    @NotNull
    private int dealerId;

    @Display("账户")
    @Size(max = DbConstant.AccountLen)
    @NotNull
    private String dealerUserName;

    @Size(max = 20)
    @Display("公司")
    private String companyName;

    @Display("负责人")
    @Size(max = DbConstant.RealNameLen)
    private String realName;

    private int productId;

    @Display("商品")
    @Size(max = 50)
    @NotNull
    private String productName;

    @Display("规格")
    @NotNull
    private int productCount;

    @Display("买平仓量【客户】")
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal customerBuyCloseCount = BigDecimal.ZERO;

    @Display("卖平仓量【客户】")
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal customerSellCloseCount = BigDecimal.ZERO;

    @Display("买转让市值差【客户】")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal customerBuyProfit = BigDecimal.ZERO;

    @Display("卖转让市值差【客户】")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal customerSellProfit = BigDecimal.ZERO;

    @Display("买平仓量【对冲】")
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal buyCloseCount = BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单

    @Display("卖平仓量【对冲】")
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal sellCloseCount = BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单

    @Display("买转让市值差【对冲】")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal buyProfit = BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单

    @Display("卖转让市值差【对冲】")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal sellProfit = BigDecimal.ZERO;//dtc合并，包括手动对冲单和自动对冲单
}
