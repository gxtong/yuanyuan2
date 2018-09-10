package com.jhx.common.util.dist.trade.dto;

/**
 * 系统间调用（通信）类型
 *
 * @author 钱智慧
 * @date 2017年9月20日 下午4:08:43
 */
public enum SysCallType {
    /**
     * 用户注册
     */
    UserRegister,

    /**
     * 发起提现
     */
    StartMoneyOut,

    /**
     * 添加Worker
     */
    AddWorker,

    /**
     * cfgDb相关的缓存更新
     */
    ConfigCacheChange,

    /**
     * 驳回提现
     */
    RejectMoneyOut,

    /**
     * 添加Customer
     */
    AddCustomer,

    /**
     * 禁止客户建仓发生变化
     */
    ForbidCustomerOpenChange,

    /**
     * 禁止客户充值发生变化
     */
    ForbidCustomerMoneyIn,

    /**
     * 禁止客户提现发生变化
     */
    ForbidCustomerMoneyOut,

    /**
     * 日终执行完操作请求后的重新加载缓存, dtc发送给dt和后台
     */
    ReloadCache,

    /**
     * 资金冲正
     */
    MoneyChange,

    /**
     * 后台测试功能, 后台发给dtc
     */
    ManageTest,

    /**
     * dtc发给manage
     */
    TradeData,

    /**
     * 测试功能：manage给dt发送行情的开关通知
     */
    ToggleTick,

    /**
     * 测试功能：manage给dt发送生成日终订单记录
     */
    GenerateTradeEndLog,

    /**
     * 测试功能：使一个客户爆仓
     */
    ForceClose,

    /**
     * 周期点位风险率 manage -> dt
     */
    RushRiskRate,

    /**
     * 订单撤销
     */
    Canceled
}
