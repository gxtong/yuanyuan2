package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 后台向dtc发的测试消息
 *
 * @author t.ch
 * @time 2017-11-29 08:40
 */
@Getter
@Setter
@Accessors(chain = true)
public class ManageTestModel {



    private TestType type;

    public enum TestType {
        /* 当日市值差结算 */
        ClearWithNoNegative,

        /* 计算服务费（开建仓）、仓储费返佣 */
        CalcChargeCommission,

        /* 计算b2c相关的所有资金：货款、税额、运费都加到admin账户上*/
        CalcB2cMoney,

        /* 执行请求记录，让操作请求生效 */
        DoOpReq,

        /* 订单归档，汇总客户交易，汇总综合经销商历史成交量 */
        SumB2bOrder,

        /* 将本交易日的资金日志归档到CustomerMoneyLog和WorkerMoneyLog，汇总资金信息CustmerMoneySum，DealerMoneySum */
        SumMoneyLog,

        /** 对账 */
        BalanceOfAccount,

        /* 更新期初资金 */
        UpdateStartMoney,

        /* 执行日终所有操作 */
        DoEndDayWork
    }
}
