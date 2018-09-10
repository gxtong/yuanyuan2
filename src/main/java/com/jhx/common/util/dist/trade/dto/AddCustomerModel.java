package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.Constant;
import com.jhx.common.util.db.DbConstant;
import com.jhx.common.util.validate.Display;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
* @author 钱智慧
* @date 2017年10月25日 下午3:40:39
*/
@Getter
@Setter
@Accessors(chain = true)
public class AddCustomerModel {


	//客户的 账户只能是手机号
	@Display("账户")
	@Pattern(regexp=Constant.PatternPhone,message="[{display}]格式不正确")
	private String phoneNum;
	
	@Display("密码")
	@Size(min =6,message="[{display}] 最小长度为{min}")
	private String password;
	
	@Display("确认密码")
	@Size(min =6,message="[{display}] 最小长度为{min}")
	private String repeatPassword;
	
	@Display("真实姓名")
	@NotBlank(message=Constant.RequiredTip)
	@Size(max = DbConstant.RealNameLen,message="[{display}] 最大长度为{max}")
	private String realName;
	
	@Display("上级")
	@NotBlank(message=Constant.RequiredTip)
	@Size(max = DbConstant.AccountLen,message="[{display}] 最大长度为{max}")
	private String refWorkerName;
	
	@Display("上级Id")
	@NotNull(message=Constant.RequiredTip)
    @Min(value = 1,message="[{display}] 数据错误")
    private int refWorkerId;
	
	@Display("分组")
    @NotNull(message=Constant.RequiredTip)
    @Min(value = 1,message="[{display}] 数据错误")
	private int groupId;
}
