package com.jhx.common.util.dist.trade.dto;

/**
 * 给客户端发送数据的类型枚举
 * 
 * @author 钱智慧
 * @date 2017年9月5日 上午12:53:50
 */
public enum ModelOutType {
	/**
	 * 调用返回
	 */
	CallReturn,
	
	/**
	 * 行情到来
	 */
	Price,
	
	/**
	 * 异地登录
	 */
	OtherLogin,
	
	/**
	 * 新闻通知
	 */
	News,
	
	/**
	 * 活动成功
	 */
	PanicBuySucc,
	
	/**
	 * 活动失败
	 */
	PanicBuyFail,
	
	/**
	 * 心跳数据
	 */
	Heartbeat,
	
	/**
	 * 连接受限
	 */
	ConnectLimit,

	/**
	 * 平仓通知
	 */
	Close,

	Restart,

	MoneyChange,

	/**
	 * 活动单（RushOrder）结束通知
	 */
	RushClose,
}
