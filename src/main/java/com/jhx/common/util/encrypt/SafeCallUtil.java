package com.jhx.common.util.encrypt;

import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.jhx.common.util.ReflectUtil;
import com.jhx.common.util.http.HttpUtil;

public class SafeCallUtil {
	
	/**
	 * 将传入的model生成签名，并将最终结果组成url参数返回
	 * @param model
	 * @param key
	 * @return
	 */
	public static String sign(SafeCallBaseModel model, String key) {
		TreeMap<String, String> map = toSortMap(model, true);
		Map<String, String> signMap = signMap(map,key);
		return HttpUtil.mapToUrlParams(signMap);
	}
	
	private static TreeMap<String,String> toSortMap(SafeCallBaseModel model,boolean ignoreSign){
		TreeMap<String, String> sortMap = new TreeMap<>();
		for (Entry<String, Object> entry : ReflectUtil.getAllFields(model).entrySet()) {
			String k = entry.getKey();
			if(ignoreSign) {
				if(k.toLowerCase().equals("sign")) {
					continue;
				}
			}
			sortMap.put(k, entry.getValue().toString());
		}
		return sortMap;
	}

	/**
	 * 对sign方法生成的数据验签，参考{@link #sign(SafeCallBaseModel, String)}
	 * @param model
	 * @param key
	 * @return
	 */
	public static boolean verify(SafeCallBaseModel model, String key) {
		String sign = sign(model, key);
		TreeMap<String, String> sortMap = toSortMap(model, false);
		return HttpUtil.mapToUrlParams(sortMap).equals(sign);
	}

	private static Map<String, String> signMap(TreeMap<String, String> sortedMapNameValueMap, String key) {
		String sign = Md5Util.md5Hex(HttpUtil.mapToUrlParams(sortedMapNameValueMap) + key);
		sortedMapNameValueMap.put("sign", sign);
		return sortedMapNameValueMap;
	}
}
