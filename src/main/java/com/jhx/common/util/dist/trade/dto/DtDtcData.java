package com.jhx.common.util.dist.trade.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * author 钱智慧
 * date 2018/1/9 20:26
 */
@Getter
@Setter
@Accessors(chain = true)
public class DtDtcData {


    private DtDtcDataType type;
    private String data;

    public DtDtcData(DtDtcDataType type, String data) {
        this.type = type;
        this.data = data;
    }
}
