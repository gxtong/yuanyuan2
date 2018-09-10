package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.dist.trade.common.entity.ClosedB2bOrderBase;
import com.jhx.common.util.dist.trade.common.entity.DealerProfitBase;
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
 * date 2017/12/11 18:09
 */
@Getter
@Setter
@Accessors(chain = true)
public class DtDtcTradeModel implements Serializable{

    //dt的public ip+port
    private String dtIpPort;

    private int totalOnlineCount;//该dt上在线总人数

    private List<CustomerRealTimeMoneyInfo> customerMoneyInfoList = new ArrayList<>();
    private List<TradeB2bOrderModel> customerOpenList = new ArrayList<>();

    //key 是 dealer id
    private Map<Integer, DealerRealTimeMoneyInfo> dealerMoneyInfoMap = new HashMap<>();

    //key是：dealer id+"#"+product id+ product count
    private Map<String,DealerTradeInfoModel> dealerTradeInfoMap = new HashMap<>();

    private List<TradeB2bOrderModel> dealerOpenList = new ArrayList<>();
    private List<TradeB2bOrderModel> dealerHedgeOpenList=new ArrayList<>();
}
