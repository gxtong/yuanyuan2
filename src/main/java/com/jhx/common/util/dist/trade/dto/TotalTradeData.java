package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author 钱智慧
 * date 2017/12/13 11:24
 */
@Getter
@Setter
@Accessors(chain = true)
public class TotalTradeData implements Serializable {

    private List<CustomerRealTimeMoneyInfo> customerMoneyInfoList = new ArrayList<>();
    private List<TradeB2bOrderModel> customerOpenList = new ArrayList<>();

    private List<SpecialDealerMoneyInfoModel> specialDealerMoneyInfoList = new ArrayList<>();
    private List<DealerRealTimeMoneyInfo> dealerMoneyInfoList = new ArrayList<>();
    private List<DealerTradeInfoModel> dealerTradeInfoList = new ArrayList<>();
    private List<TradeB2bOrderModel> dealerOpenList = new ArrayList<>();
    private List<TradeB2bOrderModel> dealerHedgeOpenList = new ArrayList<>();

    private Map<String, Integer> onlineCountMap = new HashMap<>();
}
