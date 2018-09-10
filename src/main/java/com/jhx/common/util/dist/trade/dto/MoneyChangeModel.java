package com.jhx.common.util.dist.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * 资金冲正
 *
 * @author Liu xp
 * @date 2017/12/1 9:24
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MoneyChangeModel {


    // Customer或者Worker的Id
    @Min(value = 1)
    private int id;

    //冲正金额
    @NotNull
    @Range(min = -9999999, max = 9999999)
    private BigDecimal money = BigDecimal.ZERO;

    //冲正原因
    @NotBlank
    private String note;

    //操作者ip
    private String ip;

    //操作者账户
    private String userName;

    // 是否是人工渠道, 1 充值, 2提现
    private int manual;
}
