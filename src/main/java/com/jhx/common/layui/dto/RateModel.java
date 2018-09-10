package com.jhx.common.layui.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * date 7/13/18 11:27 AM
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RateModel {
    private int integer_num;
    private int fraction_num;
    private Object min;
    private Object minInclusive;
    private Object max;
    private Object maxInclusive;
}
