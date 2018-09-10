package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 设置上级
 *
 * @author t.ch
 * @time 2017-11-24 14:44
 */
@Getter
@Setter
@Accessors(chain = true)
public class SetParentModel {


    private int workerId;
    private int newParentWorkerId;
    private String newParentWorkerName;
}
