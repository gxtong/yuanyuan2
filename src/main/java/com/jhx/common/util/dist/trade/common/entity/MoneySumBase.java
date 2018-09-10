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
import java.time.LocalDateTime;

/**
 * desc 资金汇总基类
 * author 钱智慧
 * date 2017/11/11 16:06
 */
@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
public class MoneySumBase extends IdEntityBase{


    @Display("结算日期")
    @Column(columnDefinition="datetime(3)")
    @NotNull
    private LocalDateTime sumDate;

    private int userId;
    
    @Display("账户")
    @NotNull
    @Size(max= DbConstant.AccountLen)
    private String userName;

    /**
     * 视具体情况也可能显示为：负责人
     */
    @Display("真实姓名")
    @NotNull
    @Size(max=DbConstant.RealNameLen)
    private String userRealName;

    @Display("期初资金")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal startMoney = BigDecimal.ZERO;

    @Display("冲正金额")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal changeMoney = BigDecimal.ZERO;

    @Display("充值金额")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal inMoney = BigDecimal.ZERO;

    @Display("充值手续费")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal inMoneyCharge = BigDecimal.ZERO;

    @Display("提现金额")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal outMoney = BigDecimal.ZERO;

    @Display("提现手续费")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal outMoneyCharge = BigDecimal.ZERO;

    @Display("B2C")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal b2cMoney = BigDecimal.ZERO;

    /**对客户来说，就是其平仓单的转让市值差，对dealer来说只包括手动单的转让市值差（不包手自动对冲单）**/
    @Display("转让市值差")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal closeProfit = BigDecimal.ZERO;

    @Display("信息服务费")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal openChargeMoney = BigDecimal.ZERO;

    @Display("转让服务费")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal closeChargeMoney = BigDecimal.ZERO;

    @Display("仓储费")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal delayChargeMoney = BigDecimal.ZERO;

    @Display("B2B提货货款")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal b2bPick = BigDecimal.ZERO;

    @Display("B2B运费")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal b2bDeliveryCharge = BigDecimal.ZERO;

    @Display("B2B税额")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal b2bTariff = BigDecimal.ZERO;

    //对客户来说，就是其持仓单的结算盈亏，对dealer来说只包括手动单的结算盈亏（不包手自动对冲单）
    @Display("结算盈亏")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal endProfit = BigDecimal.ZERO;

    @Display("期末资金")
    @NotNull
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    private BigDecimal endMoney = BigDecimal.ZERO;

    @Display("建仓数量")
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal openCount = BigDecimal.ZERO;

    @Display("平仓数量")
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal closeCount = BigDecimal.ZERO;

    @Display("结算数量")
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal sumCount = BigDecimal.ZERO;
}
