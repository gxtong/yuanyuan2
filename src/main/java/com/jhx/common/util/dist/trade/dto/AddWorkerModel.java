package com.jhx.common.util.dist.trade.dto;

import com.jhx.common.util.Constant;
import com.jhx.common.util.validate.Display;
import com.jhx.common.util.validate.PositiveRate;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
* @author 钱智慧
* @date 2017年10月25日 下午4:13:37
*/
@Getter
@Setter
@Accessors(chain = true)
public class AddWorkerModel {


	@Display("特别经销商Id")
    @NotNull(message = Constant.RequiredTip)
    @Min(value = 0, message = "[{display}] 数据错误")
    private int specialDealerId;//添加综合经销商时考虑

    @Display("特别经销商")
    private String specialDealerUserName; //添加综合经销商时考虑

    @Display("上级Id")
    @NotNull(message=Constant.RequiredTip)
    @Min(value = 1,message="[{display}] 数据错误")
    private int parentWorkerId; // 上级Id必填
    
	@Display("上级")
	@NotBlank(message="[{display}] 必填")
	private String parentWorkerName; // 上级
	
	@Display("用户名")
	@NotBlank(message="[{display}] 必填")
	private String userName; // 账号
	
	@Display("密码")
	@Size(min =6,message="[{display}] 最小长度为{min}")
	private String password; // 密码
	
	@Display("确认密码")
	@Size(min =6,message="[{display}] 最小长度为{min}")
	private String ensurePassword; // 确认密码

	@Display("服务费返佣比例")
	@NotNull(message = "[{display}] 必填")
	@PositiveRate(canBeZero = true, message = "请输入有效[ {display} ]")
	private BigDecimal chargeBackRate = BigDecimal.ZERO;

	@Display("服务费返佣比例")
	@NotNull(message = "[{display}] 必填")
	@PositiveRate(canBeZero = true, message = "请输入有效[ {display} ]")
	private BigDecimal rushChargeBackRate = BigDecimal.ZERO;

	@Display("仓储费返佣比例")
	@NotNull(message = "[{display}] 必填")
	@PositiveRate(canBeZero = true, message = "请输入有效[ {display} ]")
	private BigDecimal delayChargeBackRate = BigDecimal.ZERO;
	
	@NotBlank(message="[真实姓名] 必填")
	private String realName; // 真实姓名
	
	@NotBlank(message="[手机号] 必填")
	private String phoneNum; // 手机号

	private String note; // 备注
	
	private boolean margin;//是否是保证金用户
	
	@Display("角色")
	@Min(value=1,message="[ {display} ] 参数不正确")
	private int roleId;

	@Display("公司名称")
	@Length(max = 20,message = "[{display}]长度不能超过{max}")
	private String companyName; // 公司名称 选填
}
