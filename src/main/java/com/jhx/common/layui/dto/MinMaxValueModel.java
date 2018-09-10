package com.jhx.common.layui.dto;

import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * date 6/29/18 9:04 AM
 */
@Getter
@Setter
@Accessors(chain = true)

public class MinMaxValueModel extends ValueModel{
    //是否包含边界值
    private Object inclusive = true;

    public MinMaxValueModel(boolean hasAnnotation, String msg, Object value, Object inclusive) {
        super(hasAnnotation,msg,value);
        this.inclusive=inclusive;
    }
}
