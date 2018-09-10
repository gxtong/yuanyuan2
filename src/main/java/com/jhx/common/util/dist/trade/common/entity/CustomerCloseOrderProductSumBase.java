package com.jhx.common.util.dist.trade.common.entity;

import com.jhx.common.util.db.DbConstant;
import com.jhx.common.util.db.IdEntityBase;
import com.jhx.common.util.validate.Display;
import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * desc 客户成交汇总（平仓汇总）
 * author 钱智慧
 * date 2017/11/11 11:56
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
@Accessors(chain = true)
public class CustomerCloseOrderProductSumBase extends IdEntityBase {


    @NotNull
    private int userId;

    @Display("账户")
    @Size(max = DbConstant.AccountLen)
    @NotNull
    private String userName;

    @Display("姓名")
    @Size(max = DbConstant.RealNameLen)
    @NotNull
    private String userRealName;

    @NotNull
    private int productId;

    @Display("商品")
    @Size(max = 50)
    @NotNull
    private String productName;

    @Display("规格数量")
    @NotNull
    private int productCount;

    private int workerId;

    @Display("成员")
    @NotNull
    @Size(max = DbConstant.AccountLen)
    private String workerName;

    @NotNull
    private int groupId;

    @Display("客户分组")
    @Size(max = 50)
    @NotNull
    private String groupName;

    //持仓均价
    @NotNull
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal buyPositionPrice = BigDecimal.ZERO;

    //平仓均价
    @Display("平仓均价【买单】")
    @NotNull
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal buyClosePrice = BigDecimal.ZERO;

    //买单数量
    @Display("数量【买单】")
    @NotNull
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal buyCount = BigDecimal.ZERO;

    // 总量 计算均价用到
    @Display("总量")
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal buyTotalCount = BigDecimal.ZERO;

    @Display("盈亏【买单】")
    @NotNull
    @Column(precision=DbConstant.MoneyPrecision,scale=DbConstant.MoneyScale)
    private BigDecimal buyProfit = BigDecimal.ZERO;

    @Display("总转让服务费【买单】")
    @NotNull
    @Column(precision=DbConstant.MoneyPrecision,scale=DbConstant.MoneyScale)
    private BigDecimal buyCharge = BigDecimal.ZERO;

    @NotNull
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal sellPositionPrice = BigDecimal.ZERO;

    @Display("平仓均价【卖单】")
    @NotNull
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal sellClosePrice = BigDecimal.ZERO;

    @Display("数量【卖单】")
    @NotNull
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal sellCount = BigDecimal.ZERO;

    // 总量 计算均价用到
    @Display("总量")
    @Column(precision=DbConstant.DecimalPrecision,scale=DbConstant.DecimalScale)
    private BigDecimal sellTotalCount = BigDecimal.ZERO;

    @Display("盈亏【卖单】")
    @NotNull
    @Column(precision=DbConstant.MoneyPrecision,scale=DbConstant.MoneyScale)
    private BigDecimal sellProfit = BigDecimal.ZERO;

    @Display("总转让服务费【卖单】")
    @NotNull
    @Column(precision=DbConstant.MoneyPrecision,scale=DbConstant.MoneyScale)
    private BigDecimal sellCharge = BigDecimal.ZERO;

    @Display("结算日期")
    @NotNull
    @Column(columnDefinition = "datetime")
    private LocalDateTime sumDate;

    public CustomerCloseOrderProductSumBase(int userId, String userName, String userRealName, int productId, int productCount, String productName, int workerId, String workerName, int groupId, String groupName) {
        this.userId = userId;
        this.userName = userName;
        this.userRealName = userRealName;
        this.productId = productId;
        this.productName = productName;
        this.productCount = productCount;
        this.workerId = workerId;
        this.workerName = workerName;
        this.groupId = groupId;
        this.groupName = groupName;
    }
}
