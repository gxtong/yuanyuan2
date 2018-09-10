package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

/**
* @author 钱智慧
* @date 2017年10月27日 下午3:09:05
*/
@Getter
@Setter
@Accessors(chain = true)
public class CustomerRealTimeMoneyInfo implements Serializable{

	private int userId;
	
	@Display("账户")
	private String userName;
	
	@Display("姓名")
	private String realName;
	
	@Display("是否在线")
	private boolean isOnline;
	
	private int workerId;
	
	@Display("成员")
	private String workerName;
	
	private int dealerId;
	
	@Display("综合经销商")
	private String dealerUserName;
	
	@Display("风险率")
	private BigDecimal riskRate = BigDecimal.ZERO;
	
	@Display("期初资金")
	private BigDecimal startMoney = BigDecimal.ZERO;
	
	@Display("净值")
	private  BigDecimal pureMoney = BigDecimal.ZERO;
	
	@Display("浮动盈亏")
	private  BigDecimal floatProfit = BigDecimal.ZERO;
	
	@Display("可用余额")
	private  BigDecimal canUseMoney = BigDecimal.ZERO;
	
	@Display("保证金")
	private  BigDecimal leftMargin = BigDecimal.ZERO;
	
	public CustomerRealTimeMoneyInfo(boolean isOnline,UserCacheModelBase user,UserCacheModelBase ref,UserCacheModelBase dealer) {
		setOnline(isOnline).setUserId(user.getId()).setUserName(user.getUserName()).setRealName(user.getRealName())
				.setWorkerId(user.getRefWorkerId()).setWorkerName(ref==null?"":ref.getUserName()).setDealerId(user.getDealerId())
				.setDealerUserName(dealer==null?"":dealer.getUserName()).setStartMoney(user.getStartMoney())
				.setPureMoney(user.getPureMoney()).setFloatProfit(user.getFloatProfit())
				.setCanUseMoney(user.getCanUseMoney()).setLeftMargin(user.getLeftMargin()).setRiskRate(user.getRiskRate());
	}
}
