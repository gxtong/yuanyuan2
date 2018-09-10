package com.jhx.common.util.dist.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 设置特别经销商(只对综合经销商有用)
 *
 * @author t.ch
 * @time 2018-01-15 19:49
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SetSpecialDealerModel {


    private int workerId; // 综合经销商ID
    private int newSpecialDealerId; // 特别经销商ID
    private String newSpecialDealerUserName;  // 特别经销商UserName
}
