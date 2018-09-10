package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * author 钱智慧
 * date 2017/11/23 15:24
 */
@Getter
@Setter
@Accessors(chain = true)
public class GetPositionCountResult {


    private BigDecimal buyCount = BigDecimal.ZERO;
    private BigDecimal sellCount = BigDecimal.ZERO;
}
