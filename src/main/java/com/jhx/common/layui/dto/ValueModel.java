package com.jhx.common.layui.dto;

import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * date 2018/6/29 下午10:42
 */
@Getter
@Setter
@Accessors(chain = true)

public class ValueModel extends BaseModel{
    //值
    private Object value;

    public ValueModel(boolean hasAnnotation, String msg, Object value) {
        this.setHasAnnotation(hasAnnotation);
        this.setMsg(msg);
        this.value=value;
    }
}
