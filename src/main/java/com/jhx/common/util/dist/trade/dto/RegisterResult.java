package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.dto.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* @author 钱智慧
* @date 2017年9月27日 下午2:27:49
*/
@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
public class RegisterResult extends Result{


	private int id;

	private String dbName;
	
	public RegisterResult(boolean isOk,String msg,int id, String dbName) {
		super(isOk, msg);
		this.id=id;
		this.dbName = dbName;
	}
	
	public RegisterResult(boolean isOk,String msg) {
		super(isOk, msg);
	}
}
