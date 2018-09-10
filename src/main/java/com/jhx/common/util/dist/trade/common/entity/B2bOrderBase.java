package com.jhx.common.util.dist.trade.common.entity;

import com.jhx.common.util.db.DbConstant;
import com.jhx.common.util.db.IdEntityBase;
import com.jhx.common.util.validate.Display;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

/**
* author 钱智慧
* date 2017年10月28日 上午9:02:09
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@Accessors(chain = true)
public class B2bOrderBase extends IdEntityBase{



	@NotNull
	private int userId;
	
	@Display("账户")
	@NotNull
	@Size(max=DbConstant.AccountLen)
	private String userName;

	//对customer而言是姓名，对worker而言则显示为“负责人”
	@Display("姓名")
	@NotNull
	@Size(max=DbConstant.RealNameLen)
	private String userRealName;

	@NotNull
	@Display("对冲单")
	private  boolean hedge;

	@Size(max = 20)
	@Display("公司名称")
	private String companyName;
	
	@NotNull
	private int productId;
	
	@Display("商品")
	@Size(max=50)
	@NotNull
	private String productName;

	@Size(max=20)
	@NotNull
	private String code;

	//只对customer有意义
	@NotNull
	private int workerId;

	//只对customer有意义
	@Display("成员")
	@Size(max= DbConstant.AccountLen)
	@NotNull
	private String workerUserName;

	//只对customer有意义
	@NotNull
	private int groupId;

	//只对customer有意义
	@Display("客户分组")
	@Size(max=50)
	private String groupName;

	//接单方，对customer来说是dealer，对dealer来说是specialDealer
	@NotNull
	private int receiverId;

	/**
	 * 接单方，
	 * 对Customer来说，显示为：综合经销商
	 * 对Worker来说，显示为：特别经销商
	 */
	@Display("经销商")
	@Size(max=DbConstant.AccountLen)
	private String receiverUserName;

	@Display("订单类型")
	@NotNull
	private boolean buy;
	
	@Display("规格数量")
	@NotNull
	private int productCount;

	@Display("最小交易量")
	@Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
	private BigDecimal tradeMinimum = BigDecimal.ZERO;

	@NotNull
	@Size(max = 10)
	private String productUnit;
	
	@Display("止盈价")
	@NotNull
	@Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
	private BigDecimal endGainPrice = BigDecimal.ZERO;
	
	@Display("止损价")
	@NotNull
	@Column(precision = DbConstant.DecimalPrecision, scale = DbConstant.DecimalScale)
	private BigDecimal endPainPrice = BigDecimal.ZERO;
}
