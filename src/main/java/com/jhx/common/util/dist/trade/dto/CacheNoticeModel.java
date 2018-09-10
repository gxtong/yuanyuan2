package com.jhx.common.util.dist.trade.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author 钱智慧
 * @date 2017年9月23日 上午11:51:46
 */
@Getter
@Setter
@AllArgsConstructor
@Accessors(chain = true)
public class CacheNoticeModel {


    public static enum DoType {
        AddOrUpdate,
        Delete,
        Add
    }

    public static enum EntityType {
        B2BProduct,     // B2B商品
        B2BProductPic,    // B2B商品图片
        B2CProduct,    // B2C商品
        B2CProductPic,    // B2C商品图片
        Bank,  // 银行设置
        BannerImage, // 轮播图
        Cate,  // B2C商品类型
        Channel, // 充值提现渠道
        CurrencyPair,  //
        Holiday,
        SysNotice,
        SysTime,     // 时间设置
        Solution,    // B2B方案
        SpreadSolution,  // 报价点差方案
        MarginSolution,   // 预付款方案
        ServiceChargeSolution,  // 服务费方案
        DelayChargeSolution,  // 仓储费方案
        PositionCountSolution,  // 持仓数量方案
        DelayChargeLevelCfg,  //仓储费阶梯设置
        OtherSpread,      // 其他报价点差方案
        TradeLimit,      // 交易权限方案
        Group,            // 客户分组
        Role,//角色
        Bonus,//赠金
        ChannelFile,
        ServerInfo,//服务器信息
        ConfigEnableB2b,  // 全局配置_是否开启B2B
        ConfigInOutCharge, // 充值提现手续费
        ConfigWarnRate, // 客户与经销商全局风险率设置
        ConfigUsdRmbRate, // 美元兑人民币汇率设置
        ConfigAgreement, // 注册和交易协议
        ConfigRegValCode, // 注册验证码设置
        ActivateWorker, //激活Worker时manage通知dtc
        RushProduct, // 活动商品
        RushProductCfg, // 活动商品配置
        UserMoneyType, // 用户资金类型设置
        ConfigRushOpenDelay, // 活动单下单时间间隔设置
        ConfigRushRiskRate, // 接单人活动单风险率设置
        ConfigCustomerRushMoneyMax,// 客户持仓金额最大值
    }

    private List<Integer> ids;
    private DoType doType;
    private EntityType entityType;
}
