package com.jhx.common.util.dto;

import com.jhx.common.util.StrUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Result {


    @Getter
    private boolean isOk;

    @Setter
    @Getter
    private String msg;

    public String toString() {
        return StrUtil.toStr(this);
    }

    /**
     * code的含义由具体业务来定
     */
    @Getter
    @Setter
    private int code;

    public Result(boolean isOk) {
        this.setOk(isOk);
    }

    public Result setOk(boolean ok) {
        isOk = ok;
        if (StringUtils.isBlank(msg)) {
            msg = isOk ? "成功" : "未知错误";
        }
        return this;
    }


    public Result(boolean isOk, String msg) {
        this.isOk = isOk;
        this.msg = msg;
    }

    public Result(String msg) {
        this.msg = msg;
    }
}
