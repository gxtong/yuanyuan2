package com.jhx.common.util.dto;

import com.jhx.common.util.Constant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;

/**
 * @author 钱智慧
 * date 2018/6/30 下午5:13
 */
@Getter
@Setter
@Accessors(chain = true)

public class IdModel {
    @Min(value = 1,message = Constant.BadArg)
    private int id;
}
