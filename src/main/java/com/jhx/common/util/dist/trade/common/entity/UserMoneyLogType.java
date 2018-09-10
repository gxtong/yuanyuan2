package com.jhx.common.util.dist.trade.common.entity;

import com.jhx.common.util.validate.Display;

/**
 * @author 钱智慧
 * @date 2017年9月21日 上午8:31:46
 */
public enum UserMoneyLogType {
    @Display("充值")
    MoneyIn,

    /// 提现，金额为负表示提现申请，为正表示提现驳回
    @Display("提现")
    MoneyOut,

    /// 充值手续费
    @Display("充值手续费")
    InCharge,

    /// 提现手续费
    @Display("提现手续费")
    OutCharge,

    /// B2C，所有B2C相关的
    @Display("B2C")
    B2C,

    //B2B中的信息服务费
    @Display("信息服务费")
    B2BTradeOpenCharge,

    //B2B中的转让服务费
    @Display("转让服务费")
    B2BTradeCloseCharge,

    //B2B中的下单预付款
    @Display("预付款")
    B2BTradeMargin,

    @Display("资金冲正")
    MoneyChange,

    @Display("仓储费")
    DelayChange,

    @Display("信息服务费返佣")
    B2BTradeOpenChargeCommission,

    //B2B中的转让服务费
    @Display("转让服务费返佣")
    B2BTradeCloseChargeCommission,

    @Display("仓储费返佣")
    DelayChargeCommission,

    @Display("转让市值差")
    CloseProfit,

    @Display("接单方转让市值差")
    CloseProfitOfReceiver,

    @Display("B2B提货货款")
    B2BPick,

    @Display("运费")
    DeliveryCharge,

    @Display("税额")
    Tariff,

    @Display("结算盈亏")
    EndProfit,

    @Display("接单方结算盈亏")
    EndProfitOfReceiver,

    @Display("周期单盈亏")
    RushPeroid,

    @Display("周期单服务费")
    RushPeroidCharge,

    @Display("点位单盈亏")
    RushPoint,

    @Display("点位单服务费")
    RushPointCharge,

    @Display("周期单服务费返佣")
    RushPeroidChargeCommission,

    @Display("点位单服务费返佣")
    RushPointChargeCommission
}