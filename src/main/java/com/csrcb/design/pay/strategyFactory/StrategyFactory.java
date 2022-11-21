package com.csrcb.design.pay.strategyFactory;

import com.csrcb.design.pay.strategy.PayStrategy;
import com.csrcb.design.pay.strategyEnum.StrategyEnum;

/**
 * @ClassName StrategyFactory
 * @Description 工厂模式  工厂类依靠策略枚举返回策略类
 * @Author gangye
 * @Date 2022/11/21
 */
public class StrategyFactory {
    public static PayStrategy getPayStrategy(StrategyEnum strategyEnum){
        PayStrategy payStrategy = null;
        try {
            payStrategy = (PayStrategy) Class.forName(strategyEnum.getValue()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            //异常
            throw new RuntimeException(e);
        }
        return null;
    }
}
