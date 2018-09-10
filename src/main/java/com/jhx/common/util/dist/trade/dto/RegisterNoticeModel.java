package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.encrypt.SafeCallBaseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter @Setter
@AllArgsConstructor
@Accessors(chain = true)
public class RegisterNoticeModel extends SafeCallBaseModel{


	private int newCustomerId;
	private String dbName;
}
