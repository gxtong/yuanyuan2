package com.jhx.common.util.dist.trade.common.entity;

import com.jhx.common.util.db.DbConstant;
import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * author 钱智慧
 * date 2018/1/16 14:58
 */
@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
public class ClosedB2bOrderBase extends B2bOrderBase{


    @Display("订单号")
    @Size(max=30)
    @Column(unique = true)
    private String orderNo;

    @NotNull
    private int decimalPlace;

    @NotNull
    private int tradeOrderId;

    @Display("持仓单号")
    @NotNull
    @Size(max=30)
    private String tradeOrderNo;

    //平仓的部分所占用的信息服务费：（平仓手数/建仓手数）*总信息服务费
    @Display("占用信息服务费")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal openCharge = BigDecimal.ZERO;

    //只对对冲单有意义
    private int dtServerId;

    @Display("持仓价")
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    @NotNull
    private BigDecimal positionPrice= BigDecimal.ZERO;

    @Display("平仓价")
    @NotNull
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal closePrice = BigDecimal.ZERO;

    @Display("转让市值差")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal closeProfit = BigDecimal.ZERO;

    @Display("结算盈亏")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal endProfit = BigDecimal.ZERO;

    @Display("数量")
    @NotNull
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal count = BigDecimal.ZERO;

    //反应的是：转让服务费是否已经结算返佣
    @Display("已返佣")
    @NotNull
    private boolean chargeBacked;

    @Display("平仓类型")
    @NotNull
    private CloseType closeType;

    @Display("返还保证金")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal backMargin = BigDecimal.ZERO;

    @Display("转让服务费")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal closeCharge = BigDecimal.ZERO;

    @Display("市场收取转让服务费")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal closeChargeForMarket = BigDecimal.ZERO;

    @Display("建仓时间")
    @Column(columnDefinition="datetime(3)")
    private LocalDateTime srcCreateAt;

    @Display("平仓时间")
    @Column(columnDefinition="datetime(3)")
    @NotNull
    private LocalDateTime closeAt;
}
