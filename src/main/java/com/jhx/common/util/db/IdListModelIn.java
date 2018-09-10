package com.jhx.common.util.db;

import com.jhx.common.util.Constant;
import com.jhx.common.util.validate.PositiveInt;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * date 2018/4/25 下午10:12
 */
@Getter
@Setter
@Accessors(chain = true)
public class IdListModelIn<T> extends PageBean<T>{
    //不同的业务场景下有不同的含义
    @PositiveInt(canBeZero = true,message = Constant.BadArg)
    private int id;
}
