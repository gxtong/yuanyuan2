package com.jhx.common.util.dist.trade.common.entity;

import com.jhx.common.util.validate.Display;

/**
 * 账户类型
 */
public enum AccountType {

    /**
     * 后台账户：既能登录后台，又能登录客户端
     */
    @Display("后台账户")
    Worker,

    /**
     * 客户端账户：只能登录客户端
     */
    @Display("客户端账户")
    Customer,
}
