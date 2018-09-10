package com.jhx.common.util.dist.trade.tick;

import com.jhx.common.util.db.IdEntityBase;
import com.jhx.common.util.dist.trade.dto.TickType;
import com.jhx.common.util.validate.Display;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

import static com.jhx.common.util.db.DbConstant.DecimalPrecision;
import static com.jhx.common.util.db.DbConstant.DecimalScale;

/**
 * author 钱智慧
 * date 2018/2/4 下午8:33
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class TickBase extends IdEntityBase{
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

    @Display("类型")
    @NotNull
    private TickType type;
}
