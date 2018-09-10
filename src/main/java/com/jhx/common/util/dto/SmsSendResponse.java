package com.jhx.common.util.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 253云通讯普通短信响应实体
 * @author f.jx
 * @date 2018年5月23日 上午9:28
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsSendResponse {
	/**
	 * 响应时间
	 */
	private String time;
	/**
	 * 消息id
	 */
	private String msgId;
	/**
	 * 状态码说明（成功返回空）
	 */
	private String errorMsg;
	/**
	 * 状态码（详细参考提交响应状态码）
	 */
	private String code;


	@Override
	public String toString() {
		return "SmsSingleResponse [time=" + time + ", msgId=" + msgId + ", errorMsg=" + errorMsg + ", code=" + code
				+ "]";
	}
	
	
	

}
