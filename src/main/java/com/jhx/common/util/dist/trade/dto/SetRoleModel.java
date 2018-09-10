package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * desc set role
 * author 钱智慧
 * date 2017/11/21 14:00
 */
@Getter
@Setter
@Accessors(chain = true)
public class SetRoleModel {


    private int workerId;
    private int newRoleId;
}
