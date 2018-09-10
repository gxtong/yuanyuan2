
package com.jhx.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

/**
 * 读取配置文件
 */
public class PropLoader {

    private PropLoader() {
    }

    private ResourceBundle PROP_RESOURCE = null;

    /**
     * desc 从程序根目录下加载指定的properties文件，不能有后缀"properties“
     *
     * @author 钱智慧
     * date 8/13/18 9:03 AM
     **/
    public static PropLoader load(String filePath) {
        PropLoader loader = new PropLoader();

        try (InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath))) {
            loader.PROP_RESOURCE = new PropertyResourceBundle(inputStream);
        } catch (Exception e) {
            LogUtil.errThenExit(e);
        }
        return loader;
    }

    public int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public String get(String key) {
        if (PROP_RESOURCE == null || !PROP_RESOURCE.containsKey(key)) {
            return null;
        }
        return StringUtils.trim(transformCodeIso8859Style(PROP_RESOURCE.getString(key), "UTF-8"));
    }

    /**
     * 转字符串编码
     *
     * @return String
     */
    private String transformCodeIso8859Style(String content, String charset) {
        try {
            return new String(content.getBytes("ISO-8859-1"), charset);
        } catch (UnsupportedEncodingException e) {
            LogUtil.err("配置文件字符串编码转换错误 : " + e.getMessage());
        }
        return content;
    }
}
