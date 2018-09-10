package com.jhx.common.util.dist.trade.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.jhx.common.util.Constant;
import com.jhx.common.util.db.DbConstant;

import com.jhx.common.util.validate.Display;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * @author 钱智慧
 * @date 2017年9月27日 上午10:30:05
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MoneyOutRejectModel {


    // Customer或者Worker的Id
    @Min(value = 1)
    @Max(value = Integer.MAX_VALUE, message = "被操作的用户id错误")
    private int userId;
    @Max(value = Integer.MAX_VALUE, message = "提现单id错误")
    @Min(value = 1)
    private int moneyOutId;
    private String rejectorIp;
    private String rejectorUserName;
    /**
     * 备注
     */
    @Display("备注")
    @NotNull(message = Constant.RequiredTip)
    @Size(max = DbConstant.IpLen)
    private String note;
}
