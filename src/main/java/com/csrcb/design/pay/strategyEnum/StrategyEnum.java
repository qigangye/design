package com.csrcb.design.pay.strategyEnum;

/**
 * @ClassName StrategyEnum
 * @Description 枚举
 * @Author gangye
 * @Date 2022/11/21
 */
public enum StrategyEnum {
    ZfbPayStrategy("com.csrcb.design.pay.strategy.ZfbPayStrategy"),
    WxPayStrategy("com.csrcb.design.pay.strategy.WxPayStrategy"),
    BkPayStrategy("com.csrcb.design.pay.strateg.BkPayStrategy");

    String value = "";
    StrategyEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
