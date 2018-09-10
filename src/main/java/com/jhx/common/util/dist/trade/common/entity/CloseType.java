package com.jhx.common.util.dist.trade.common.entity;

import com.jhx.common.util.validate.Display;

/**
 * author 钱智慧
 * date 2017/12/5 9:07
 */
public enum CloseType {
    @Display("止盈")
    EndGain,

    @Display("止损")
    EndPain,

    @Display("强平")
    Force,

    @Display("手动")
    Normal,

    //只对hedge=true，即对冲单，有意义
    @Display("对冲")
    Hedge,

    @Display("提货")
    Pick
}
