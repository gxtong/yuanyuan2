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

@Getter
@Setter
@MappedSuperclass
@Accessors(chain = true)
public class UserMoneyLogBase extends IdEntityBase {


    @NotNull(message = "必填")
    private int userId;

    @Size(max = DbConstant.AccountLen)
    @Display("账户")
    @NotNull
    private String userName;

    @Display("公司")
    @Size(max = 20)
    private String companyName;

    // 只对Customer有意义， 所属Worker，含义：业务上由哪个Worker发展而来
    @NotNull
    private int refWorkerId;

    @Display("成员")
    @Size(max = DbConstant.AccountLen)
    private String refWorkerName;

    /**
     * 分组ID
     */
    @NotNull
    private int groupId;

    /**
     * 对于成员来说，这一列显示为：负责人
     * 对于客户来说，这一列显示为：姓名
     */
    @Size(max = DbConstant.RealNameLen)
    @Display("姓名")
    private String realName;

    @Display("变动前余额")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal beforeMoney = BigDecimal.ZERO;

    // 变动金额，正数代表加钱，负数代表减钱
    @Display("变动金额")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal money = BigDecimal.ZERO;

    @Display("变动后余额")
    @Column(precision = DbConstant.MoneyPrecision, scale = DbConstant.MoneyScale)
    @NotNull
    private BigDecimal afterMoney = BigDecimal.ZERO;

    @Display("类型")
    @NotNull
    private UserMoneyLogType type;

    @Display("备注")
    @Size(max = DbConstant.SimpleNoteLen)
    @NotNull
    private String note;

    // 源订单号，根据类型type的不同，该值含义也不同，比如如果type为UserMoneyLogType.MoneyIn，则orderNo为充值的订单号
    @Size(max = 30)
    @Display("订单号")
    private String orderNo;

}
