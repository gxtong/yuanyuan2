package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 设置分组
 *
 * @author Liu xp
 * @date 2017/12/5 16:59
 */
@Getter
@Setter
@Accessors(chain = true)
public class SetGroupModel {


    private int customerId;
    private int newGroupId;
}
