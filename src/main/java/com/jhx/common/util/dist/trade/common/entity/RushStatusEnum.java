package com.jhx.common.util.dist.trade.common.entity;

import com.jhx.common.util.validate.Display;

/**
 * author 趙帥尅
 * date 2018/04/17 下午05:19
 */
public enum  RushStatusEnum {

    @Display("进行中")
    Trading,

    @Display("完成")
    Complete,

    @Display("退单")
    Undo,
}
