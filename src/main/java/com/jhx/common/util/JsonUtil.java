package com.jhx.common.util;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
* @author 钱智慧
* @date 2017年9月2日 下午4:29:42
*/
public class JsonUtil {
	/**
	 * 判断json字符 串是否合法
	 * @param jsonStr
	 * @return boolean
	 * @author 钱智慧
	 * @date 2017年9月2日 下午3:48:53
	 */
	public static boolean isJson(String jsonStr) {
		try {
			new Gson().fromJson(jsonStr, Object.class);
			return true;
		}catch (JsonSyntaxException ex){
			return false;
		}
	}
}
