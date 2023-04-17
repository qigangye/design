package com.csrcb.design.pay.factory;

import com.csrcb.design.pay.strategy.PayStrategy;
import com.csrcb.design.pay.strategyEnum.StrategyEnum;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName StrategyFactory
 * @Description 工厂模式  工厂类依靠策略枚举返回策略类
 * @Author gangye
 * @Date 2022/11/21
 */
public class StrategyFactory {
    private static final Map<String, PayStrategy> strategyMaps = new ConcurrentHashMap<>();
    public static PayStrategy getPayStrategy(StrategyEnum strategyEnum){
        PayStrategy payStrategy = strategyMaps.get(strategyEnum.getValue());
        if (null == payStrategy) {
            try {
                // 每次获取都需要进行一次反射。需要优化调整(结合单例模式)
                payStrategy = (PayStrategy) Class.forName(strategyEnum.getValue()).newInstance();
                strategyMaps.put(strategyEnum.getValue(), payStrategy);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                //异常
                throw new RuntimeException(e);
            }
        }
        return payStrategy;
    }
}
