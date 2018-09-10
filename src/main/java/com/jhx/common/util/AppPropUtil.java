
package com.jhx.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 读取配置文件
 */
public class AppPropUtil {

    private static ResourceBundle APP_PROP_RESOURCE = null;

    static {
        try {
            APP_PROP_RESOURCE = ResourceBundle.getBundle("application");
        } catch (MissingResourceException e) {
            LogUtil.info("缺少配置文件");
        }
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static String get(String key) {
        if (APP_PROP_RESOURCE==null || !APP_PROP_RESOURCE.containsKey(key)) {
            return null;
        }
        return StringUtils.trim(transformCodeIso8859Style(APP_PROP_RESOURCE.getString(key), "UTF-8"));
    }

    /**
     * 转字符串编码
     *
     * @param content
     * @param charset
     * @return String
     */
    public static String transformCodeIso8859Style(String content, String charset) {
        try {
            return new String(content.getBytes("ISO-8859-1"), charset);
        } catch (UnsupportedEncodingException e) {
            LogUtil.err("配置文件字符串编码转换错误 : " + e.getMessage());
        }
        return content;
    }
}
