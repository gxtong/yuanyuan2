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
 * desc 仓单父类
 * author 钱智慧
 * date 2017/11/11 14:45
 */
@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
public class TradeB2bOrderBase extends B2bOrderBase {


    @Display("订单号")
    @Size(max = 30)
    @Column(unique = true)
    private String orderNo;

    //只对对冲单有意义
    private int dtServerId;

    //该订单上方的特别经销商
    @NotNull
    private int specialDealerId;

    @NotNull
    private int decimalPlace;

    // 延期天数, 只算交易日
    @Display("延期天数")
    @NotNull
    private int delayDays;

    //1手的总结算盈亏
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal unitEndProfit = BigDecimal.ZERO;

    @NotNull
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal basePrice = BigDecimal.ZERO;

    /**
     * 若是当日的单子，则持仓价和建仓价相等
     * 若是隔日的单子，则持仓价和结算价相等
     */
    @Display("持仓价")
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    @NotNull
    private BigDecimal positionPrice = BigDecimal.ZERO;

    //信息服务费是否已返佣
    @Display("信息服务费已返佣")
    @NotNull
    private boolean openChargeBacked;

    //只返那些leftCount=0即全部平仓的持仓单
    @Display("仓储费已返佣")
    @NotNull
    private boolean delayChargeBacked;

    @Display("持仓数量")
    @NotNull
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal leftCount = BigDecimal.ZERO;

    @Display("建仓数量")
    @NotNull
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal openCount = BigDecimal.ZERO;

    @Display("提货数量")
    @NotNull
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    private BigDecimal pickCount = BigDecimal.ZERO;

    /**
     * desc 判断是否真正平仓
     * author 钱智慧
     * date 2018/1/23 19:14
     **/
    public boolean hasRealClosed() {
        return leftCount.compareTo(BigDecimal.ZERO) <= 0;
    }

    @Display("总预付款")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal totalMargin = BigDecimal.ZERO;

    @Display("剩余预付款")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal leftMargin = BigDecimal.ZERO;

    @Display("信息服务费")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal openCharge = BigDecimal.ZERO;

    @Display("1手信息服务费")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal unitOpenCharge = BigDecimal.ZERO;

    @Display("市场收取信息服务费")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal openChargeForMarket = BigDecimal.ZERO;

    @Display("仓储费")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal delayCharge = BigDecimal.ZERO;

    @Display("市场收取仓储费")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal delayChargeForMarket = BigDecimal.ZERO;

    @Display("建仓价")
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    @NotNull
    private BigDecimal openPrice = BigDecimal.ZERO;

    @Display("点差")
    @Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
    @NotNull
    private BigDecimal spread = BigDecimal.ZERO;

    @Size(max = 30)
    @Display("指价单号")
    @Column(unique = true)
    private String delegateOrderNo;

    /**
     * 结单时间，即全部平仓的时间
     */
    @Display("结单时间")
    @Column(columnDefinition = "datetime(3)")
    private LocalDateTime finishAt;
}
