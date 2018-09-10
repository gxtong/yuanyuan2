package com.jhx.common.util.dist.trade.common.entity;

import com.jhx.common.util.validate.Display;

/**
 * author 钱智慧
 * date 2017/12/10 下午10:57
 */
public enum RoleType {
    @Display("平台")
    Platform,

    @Display("特别经销商")
    SpecialDealer,

    @Display("综合经销商")
    Dealer,

    @Display("其他")
    Other
}
