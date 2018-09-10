package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.dist.trade.common.entity.TradeB2bOrderBase;
import com.jhx.common.util.validate.Display;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * @author 钱智慧
 * @date 2017年11月2日 下午2:17:58
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class TradeB2bOrderModel extends TradeB2bOrderBase {


    @Display("现价")
    private BigDecimal nowPrice = BigDecimal.ZERO;

    @Display("浮动盈亏")
    private BigDecimal floatProfit = BigDecimal.ZERO;
}
