package com.jhx.common.util.dist.trade;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

/**
 * author 钱智慧
 * date 2017/12/20 18:10
 */
@Getter
@Setter
@Accessors(chain = true)
public class OrderNoUtil {
    private static ThreadLocal<Random> rnd = new ThreadLocal<>();

    public static String generate(int userId, int entityId) {
        String orderNo = userId + "A" + entityId;
        int start = 10;
        while (orderNo.length() > start) {
            start++;
        }
        int len=start-orderNo.length();
        if(len>0){
            String str=String.valueOf(System.currentTimeMillis());
            orderNo=str.substring(str.length()-len)+orderNo;
        }
        orderNo = StringUtils.leftPad(orderNo, start, '0');
        return orderNo;
    }
}
