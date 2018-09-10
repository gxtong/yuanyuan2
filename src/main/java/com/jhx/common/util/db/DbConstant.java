package com.jhx.common.util.db;

import com.google.common.collect.Lists;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 封装一些常用的常量
 */
public class DbConstant {
    public static final List<String> OrderDirections = Lists.newArrayList("desc", "asc");
    public static String Desc = OrderDirections.get(0);
    public static String Asc = OrderDirections.get(1);
    public static String DbFailMsg = "数据库操作失败";
    /**
     * 账户字段的长度
     */
    public static final int AccountLen = 20;

    /**
     * 密码字段的长度
     */
    public static final int PasswordLen = 48;

    /**
     * 真实姓名长度
     */
    public static final int RealNameLen = 30;

    /**
     * 金额字段的整数位数
     */
    public static final int MoneyPrecision = 15;

    /**
     * 金额字段的小数位数
     */
    public static final int MoneyScale = 2;

    /**
     * 非金额小数字段的整数位数
     */
    public static final int DecimalPrecision = 13;

    /**
     * 非金额小数字段的小数位数
     */
    public static final int DecimalScale = 8;


    /**
     * 比例小数 整数位数
     */
    public static final int RatePrecision = 13;

    /**
     * 比例小数 小数位数
     */
    public static final int RateScale = 4;

    /**
     * ip字段的长度
     */
    public static final int IpLen = 30;

    public static final int PhoneLen = 20;

    public static final int SimpleNoteLen = 200;

    /**
     * 普通名称字段的长度
     */
    public static final int SimpleNameLen=20;

    public static final int DetailNoteLen = 600;

    public static final int UrlLen = 200;

    /**
     * 银行编码长度
     */
    public static final int BankCodeLen = 10;

    /**
     * 支行长度
     */
    public static final int BankBranchLen = 100;

    /**
     * 银行名称长度
     */
    public static final int BankNameLen = 15;

    /**
     * 银行卡号长度
     */
    public static final int BankCardLen = 25;

    /**
     * 省长度
     */
    public static final int ProvinceLen = 20;

    /**
     * 市长度
     */
    public static final int CityLen = 100;

    public static final Map<String, String> BankMap = new HashMap<String, String>() {
        /**
         *
         */
        private static final long serialVersionUID = -2851670404925698068L;

        {
            put("icbc", "工商银行");
            put("ccb", "建设银行");
            put("abc", "农业银行");
            put("comm", "交通银行");
            put("cmb", "招商银行");
            put("spdb", "浦发银行");
            put("cib", "兴业银行");
            put("boc", "中国银行");
            put("post", "邮政储蓄");
            put("ecitic", "中信银行");
            put("cmbc", "民生银行");
            put("ceb", "光大银行");
            put("hxb", "华夏银行");
            put("pab", "平安银行");
            put("gdb", "广发银行");
        }
    };
}
