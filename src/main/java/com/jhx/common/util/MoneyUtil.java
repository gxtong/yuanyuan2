package com.jhx.common.util;

import com.jhx.common.util.db.DbConstant;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * author 钱智慧
 * date 2018/1/26 10:11
 */
public class MoneyUtil {
    
    /**
     * desc 对结果金额进行“银行家舍入”算法，以减少精度损失
     * author 钱智慧
     * date 2018/1/26 10:13
     **/
    public static BigDecimal setScale(BigDecimal srcValue){
        return srcValue.setScale(DbConstant.MoneyScale,RoundingMode.HALF_EVEN);
    }
}
