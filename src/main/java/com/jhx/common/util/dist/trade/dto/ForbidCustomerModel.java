package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * author 钱智慧
 * date 2017/11/24 14:24
 */
@Getter
@Setter
@Accessors(chain = true)
public class ForbidCustomerModel {
    private int customerId;
    private boolean forbid;
}
