package com.jhx.common.util.dist.trade.dto;

import com.alibaba.druid.support.spring.stat.annotation.StatAnnotationAdvisor;
import com.google.gson.Gson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 系统间通信传输的数据
* @author 钱智慧
* @date 2017年9月20日 下午4:13:18
*/
@Data
@AllArgsConstructor
@Accessors(chain = true)
public class SysCallData {


	private SysCallType type;
	private String jsonData;
	
	public static String toJson(SysCallType type,Object obj) {
		Gson gson = new Gson();
		String data = gson.toJson(obj);
		SysCallData sysCallData = new SysCallData(type, data);
		return gson.toJson(sysCallData);
	}

	public static String toJson(SysCallType type,String content) {
		Gson gson = new Gson();
		SysCallData sysCallData = new SysCallData(type, content);
		return gson.toJson(sysCallData);
	}
	
	public static SysCallData fromJson(String jsonData) {
		Gson gson = new Gson();
		return gson.fromJson(jsonData, SysCallData.class);
	}
}
