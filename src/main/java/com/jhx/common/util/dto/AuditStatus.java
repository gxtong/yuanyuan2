package com.jhx.common.util.dto;

import com.jhx.common.util.validate.Display;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * date 6/15/18 10:23 AM
 */
public enum  AuditStatus {
    @Display("待审核")
    Waiting,

    @Display("已驳回")
    Rejected,

    @Display("通过")
    Approved,
}
