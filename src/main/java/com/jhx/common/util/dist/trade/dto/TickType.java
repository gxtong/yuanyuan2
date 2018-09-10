package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.validate.Display;

/**
 * desc k线类型
 * author 钱智慧
 * date 2017/11/14 14:16
 */
public enum TickType{
    @Display("1分钟线")
    M1,

    @Display("5分钟线")
    M5,

    @Display("15分钟线")
    M15,

    @Display("30分钟线")
    M30,

    @Display("1小时线")
    H1,

    @Display("4小时线")
    H4,

    @Display("日线")
    D,

    @Display("周线")
    W,

    @Display("月线")
    MN
}
