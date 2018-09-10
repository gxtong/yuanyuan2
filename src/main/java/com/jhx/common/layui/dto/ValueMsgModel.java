package com.jhx.common.layui.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * date 7/13/18 3:39 PM
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ValueMsgModel {
    private Object value;
    private String msg;
}
