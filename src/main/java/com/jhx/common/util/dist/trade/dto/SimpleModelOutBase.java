package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.dto.Result;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * date 2018/5/23 下午8:55
 */
@Getter
@Setter
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class SimpleModelOutBase extends Result{
    private ModelOutType type;
    private String srData;

    public SimpleModelOutBase(boolean isOk,ModelOutType type) {
        super(isOk);
        this.type=type;
    }

    public SimpleModelOutBase(boolean isOk) {
        super(isOk);
    }

    public SimpleModelOutBase(boolean isOk, String msg) {
        super(isOk, msg);
    }

    public SimpleModelOutBase(String srData,boolean isOk,String msg){
        super(isOk,msg);
        this.srData=srData;
    }

    public SimpleModelOutBase(boolean isOk, String msg, ModelOutType type) {
        super(isOk, msg);
        this.type=type;
    }
}
