package com.jhx.common.util.dist.trade.tick;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

/**
 * 报价类
 *
 * @author 钱智慧
 * @date 2017年10月30日 上午10:53:53
 */
@Getter
@Setter
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TickItem {
    @JsonProperty("Price")
    private BigDecimal price = BigDecimal.ZERO;

    @JsonProperty("Time")
    private int time;

    @JsonProperty("Code")
    private String code;
}
