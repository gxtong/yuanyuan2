package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.dto.Result;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
* @author 钱智慧
* @date 2017年10月25日 下午4:21:12
*/
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
public class AddUserResult extends Result{


    private int newUserId;
	private String dbName;
	
	public AddUserResult(boolean isOk,String msg, int newUserId, String dbName) {
        super(isOk, msg);
        this.newUserId = newUserId;
        this.dbName = dbName;
    }
	
	public AddUserResult(boolean isOk,String msg) {
        super(isOk, msg);
    }
}
