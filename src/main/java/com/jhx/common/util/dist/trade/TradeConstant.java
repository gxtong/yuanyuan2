package com.jhx.common.util.dist.trade;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * author 钱智慧
 * date 2017/12/4 15:01
 */
@Getter
@Setter
@Accessors(chain = true)
public class TradeConstant {


    public final static BigDecimal RiskMax = new BigDecimal(10000);//风险值极大值
    public final static BigDecimal RiskMin = new BigDecimal(-10000);//风险值极小值
}
