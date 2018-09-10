package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

import static com.jhx.common.util.db.DbConstant.*;

/**
 * desc k线数据模型类
 * author 钱智慧
 * date 2017/11/13 11:29
 */
@Getter
@Setter
@Accessors(chain = true)
public class TickModel {


    @Display("开盘价")
    @NotNull
    @Column(precision = DecimalPrecision, scale = DecimalScale)
    private BigDecimal openPrice = BigDecimal.ZERO;

    @Display("最高价")
    @NotNull
    @Column(precision = DecimalPrecision, scale = DecimalScale)
    private BigDecimal highPrice = BigDecimal.ZERO;

    @Display("最低价")
    @NotNull
    @Column(precision = DecimalPrecision, scale = DecimalScale)
    private BigDecimal lowPrice = BigDecimal.ZERO;

    @Display("收盘价")
    @NotNull
    @Column(precision = DecimalPrecision, scale = DecimalScale)
    private BigDecimal closePrice = BigDecimal.ZERO;

    /**
     * unix时间戳
     */
    @Display("时间")
    @NotNull
    private int time;

    /**
     * 来自MT4
     */
    @Display("编码")
    @NotNull
    @Size(max = 20)
    private String code;
}
