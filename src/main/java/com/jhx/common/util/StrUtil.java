package com.jhx.common.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * String 工具类
* @author 钱智慧
* @date 2017年9月2日 上午8:34:07
*/
public class StrUtil {
	private static final String SEP1 = ",";  
	private static final String SEP2 = "=";  

	/**
	 * 将对象转为string，如{name=rose,age=2}，
	 * 如果对象是List&lt;?&gt;则为：[{name=jack,age=10},{name=jack1,age=11}]，
	 * 不考虑map类型，不考虑对象内部的对象属性的递归情况，并且假设对象中的属性都是基本类型
	* @param obj
	* @return
	* @author 钱智慧
	* @date 2017年9月2日 上午9:46:35
	 */
	public static String toStr(Object obj) {
		if(obj instanceof List<?>) {
			return list2Str((List<?>)obj);
		}else {
			return obj2Str(obj);
		}
	}
	
	/**
	 * 订单号由用户id和订单id组成，共16位<br/>
	 * 前7位是用户id，不足7为在左边补0<br/>
	 * 后9位是订单id，不足9位在左边补0<br/>
	 * 共16位<br/>
	 * @param userId  用户id
	 * @param outId	 订单id
	 * @return String 组装成的订单号
	 */
	public static String  orderNo(int userId,int outId) {
		return String.format("%07d%09d", userId, outId);
	}

	/**
	 * 将逗号分隔的字符串转成整数List
	 * @param commaStr
	 * @return
	 */
	public static List<Integer> toIntList(String commaStr){
		List<Integer> ret=new ArrayList<>();
		if(StringUtils.isNotBlank(commaStr)){
			Arrays.stream(StringUtils.split(commaStr, ',')).forEach(item->ret.add(Integer.parseInt(item)));
		}
		return ret;
	}

	private static String list2Str(List<?> list) {
		StringJoiner sj=new StringJoiner(SEP1,"[","]");
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i) == null || list.get(i) == "") {
					continue;
				}
				if (list.get(i) instanceof List) {
					sj.add(list2Str((List<?>) list.get(i)));
				} else {
					sj.add(obj2Str(list.get(i)));
				}
			}
		}
		return sj.toString();
	}


	private static String obj2Str(Object obj) {
		StringJoiner sj=new StringJoiner(SEP1,"{","}");
		Map<String, Object> map = ReflectUtil.getAllFields(obj);
		for(Entry<String, Object> entry:map.entrySet()){
			Object value = entry.getValue();
			String valStr="";
			if(value instanceof BigDecimal) {
				valStr=String.valueOf(((BigDecimal)value).doubleValue());
			}else {
				valStr=value.toString();
			}
			sj.add(entry.getKey()+SEP2+valStr);
		}
		return sj.toString();
	}
}
