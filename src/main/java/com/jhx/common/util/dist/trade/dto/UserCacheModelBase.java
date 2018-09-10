package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.SafeDecimal;
import com.jhx.common.util.dist.trade.TradeConstant;
import com.jhx.common.util.dist.trade.common.entity.AccountType;
import com.jhx.common.util.dist.trade.common.entity.ClosedB2bOrderBase;
import com.jhx.common.util.dist.trade.common.entity.RoleType;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * author 钱智慧
 * date 2017/12/10 下午5:36
 */
@Getter
@Setter
@Accessors(chain = true)
public class UserCacheModelBase implements Serializable {


    private int id;
    private String userName;
    private String realName;
    private String companyName;
    private int parentWorkerId;
    private RoleType roleType;
    private int roleId;//只对Worker有意义
    private int refWorkerId;
    private int dealerId;//对Customer才有意义，其上方最近的dealer
    private int specialDealerId;//对Dealer有意义，其上方最近的specialDealer
    private AccountType type;//标识是Worker还是Customer
    private boolean isOnline;
    private int groupId;//只对Customer有意义

    private boolean forbidOpen; // 禁止建仓
    private boolean forbidMoneyOut; // 禁止提现
    private boolean forbidMoneyIn;  // 禁止充值
    private volatile boolean frozen;

    //余额：对应着实体中money，这里主要用于实时的计算净值
    private BigDecimal money = BigDecimal.ZERO;

    //对Customer才有意义 可用余额
    private BigDecimal canUseMoney = BigDecimal.ZERO;

    //对Customer才有意义 期初资金
    private BigDecimal startMoney = BigDecimal.ZERO;

    private BigDecimal baseMoney = BigDecimal.ZERO;//只对dealer 和special dealer有意义
    private BaseMoneyType baseMoneyType;//只对dealer 和special dealer有意义

    //自己下的持仓单
    private Queue<TradeB2bOrderModel> openQueue = new ConcurrentLinkedQueue<>();

    private BigDecimal leftMargin = BigDecimal.ZERO;

    //净值
    private BigDecimal pureMoney = BigDecimal.ZERO;//只对Customer有用：净值=余额+floatProfit-保证金

    //持仓单浮动盈亏
    private BigDecimal floatProfit = BigDecimal.ZERO;//openQueue的sum(floatProfit)

    //持仓单当日转让市值差
    private SafeDecimal closeProfit = new SafeDecimal();

    //自动对冲单浮动盈亏 ，只对综合经销商有意义
    private BigDecimal hedgeFloatProfit = BigDecimal.ZERO;

    //自动对冲单转让市值差，只对综合经销商有意义
    private SafeDecimal hedgeCloseProfit = new SafeDecimal();

    //自动对冲的持仓单 ，只对综合经销商有意义
    private Queue<TradeB2bOrderModel> hedgeOpenQueue = new ConcurrentLinkedQueue<>();

    //当日自动对冲的平仓单 ，只对综合经销商有意义
    private Queue<ClosedB2bOrderBase> hedgeCloseQueue = new ConcurrentLinkedQueue<>();

    //风险值：Customer和Dealer的风险计算方式不一样，Customer由本地实时计算，但Dealer的风险值则由dtc发过来
    private BigDecimal riskRate = BigDecimal.ZERO;

    //风控概率, 只对综合经销商有效
    private BigDecimal manageRiskRate = BigDecimal.ZERO;

    public boolean isCustomer() {
        return getType().equals(AccountType.Customer);
    }

    public boolean isDealer() {
        return getType() != null && getRoleType() != null && getType().equals(AccountType.Worker) && getRoleType().equals(RoleType.Dealer);
    }

    public boolean isSpecialDealer() {
        return getType() != null && getRoleType() != null && getType().equals(AccountType.Worker) && getRoleType().equals(RoleType.SpecialDealer);
    }

    /**
     * desc 入队后，但db平仓前调用===针对自动触发平仓
     * author 钱智慧
     * date 2018/1/17 19:21
     **/
    public void closeOrderInMemory(BigDecimal closeCount, TradeB2bOrderModel order, BigDecimal backMargin) {
        order.setLeftCount(order.getLeftCount().subtract(closeCount)).setLeftMargin(order.getLeftMargin().subtract(backMargin));
        if (order.getLeftCount().signum() == 0) {
            openQueue.remove(order);
        }
        leftMargin = leftMargin.subtract(backMargin);
    }

    /**
     * desc db平仓后调用==针对自动触发平仓
     * author 钱智慧
     * date 2018/1/17 19:21
     **/
    public void afterCloseOrderInDbForAuto(ClosedB2bOrderBase closeOrder) {
        closeProfit.add(closeOrder.getCloseProfit());
    }

    /**
     * desc db平仓后调用==针对手动平仓
     * author 钱智慧
     * date 2018/1/17 19:21
     **/
    public void afterCloseOrderInDbForManual(TradeB2bOrderModel srcOrder, ClosedB2bOrderBase closeOrder) {
        srcOrder.setLeftCount(srcOrder.getLeftCount().subtract(closeOrder.getCount())).setLeftMargin(srcOrder.getLeftMargin().subtract(closeOrder.getBackMargin()));
        if (srcOrder.hasRealClosed()) {
            openQueue.remove(srcOrder);
        }
        closeProfit.add(closeOrder.getCloseProfit());
    }

    public void openOrder(TradeB2bOrderModel order) {
        openQueue.add(order);
    }

    public void calcRiskAndPureMoney() {
        setPureMoney(getMoney().add(getFloatProfit()));
        if (leftMargin.compareTo(BigDecimal.ZERO) == 0) {
            if (getPureMoney().compareTo(BigDecimal.ZERO) > 0) {
                setRiskRate(TradeConstant.RiskMax);
            } else if (getPureMoney().compareTo(BigDecimal.ZERO) == 0) {
                setRiskRate(BigDecimal.ZERO);
            } else {
                setRiskRate(TradeConstant.RiskMin);
            }
        } else {
            setRiskRate(getPureMoney().divide(leftMargin, 4, RoundingMode.DOWN));
        }
    }

    public void openHedgeOrder(TradeB2bOrderModel hedgeOrderModel) {
        hedgeOpenQueue.add(hedgeOrderModel);
    }

    /**
     * desc db平仓后调用
     * author 钱智慧
     * date 2018/1/23 19:17
     **/
    public void closeHedgeOrder(TradeB2bOrderModel hedgeOrderModel, ClosedB2bOrderBase closeOrder) {
        hedgeOrderModel.setLeftCount(hedgeOrderModel.getLeftCount().subtract(closeOrder.getCount()));
        if (hedgeOrderModel.hasRealClosed()) {
            hedgeOpenQueue.remove(hedgeOrderModel);
        }
        hedgeCloseQueue.add(closeOrder);
        hedgeCloseProfit.add(closeOrder.getCloseProfit());
    }
}
