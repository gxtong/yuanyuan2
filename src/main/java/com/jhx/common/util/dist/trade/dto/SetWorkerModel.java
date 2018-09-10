package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * author 钱智慧
 * date 2017/11/22 13:48
 */
@Getter
@Setter
@Accessors(chain = true)
public class SetWorkerModel {


    private int customerId;
    private int newWorkerId;
}
