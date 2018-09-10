package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class ModelOutBase extends SimpleModelOutBase {
    private int userId;

    public ModelOutBase(boolean isOk, ModelOutType type) {
        super(isOk,type);
    }

    public ModelOutBase(boolean isOk, ModelOutType type, String msg) {
        super(isOk, msg,type);
    }

    public ModelOutBase(boolean isOk) {
        super(isOk);
    }

    public ModelOutBase(boolean isOk, String msg) {
        super(isOk, msg);
    }

    public ModelOutBase(String srData, boolean ok, String msg) {
        super(srData,ok,msg);
    }
}
