package com.jhx.common.layui.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * date 6/29/18 9:04 AM
 */
@Getter
@Setter
@Accessors(chain = true)

public class RegExModel extends BaseModel{

    //正则表达式
    private String expr;

    public RegExModel(boolean hasAnnotation, String msg, String expr) {
        setHasAnnotation(hasAnnotation);
        setMsg(msg);
        this.expr=expr;
    }
}
