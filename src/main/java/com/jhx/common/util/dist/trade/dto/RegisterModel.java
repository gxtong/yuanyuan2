package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.Constant;
import com.jhx.common.util.encrypt.SafeCallBaseModel;
import com.jhx.common.util.validate.Display;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Accessors(chain = true)
public class RegisterModel  extends SafeCallBaseModel {


	@Display("账户")
	@NotBlank(message=Constant.RequiredTip)
	@Pattern(regexp="^1[0-9]{10}$",message="手机号格式不正确")
	private String userName;
	
	@Display("密码")
	@NotBlank(message=Constant.RequiredTip)
	private String password;
	
	@Display("重复密码")
	@NotBlank(message=Constant.RequiredTip)
	private String repeatPassword;
	
	@Display("推荐码")
	private int refWorkerId;
	
	@Display("真实姓名")
	private String realName;

	@Display("身份证号")
	private String idNo;
	
	@Display("客户分组ID")
	private int groupId;
}
