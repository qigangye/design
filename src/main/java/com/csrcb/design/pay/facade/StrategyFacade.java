package com.csrcb.design.pay.facade;

import com.csrcb.design.pay.additionalDecorator.AddFuncDecorator;
import com.csrcb.design.pay.pojo.PayBody;
import com.csrcb.design.pay.strategy.PayStrategy;
import com.csrcb.design.pay.strategyContext.PayContext;
import com.csrcb.design.pay.strategyEnum.StrategyEnum;
import com.csrcb.design.pay.strategyFactory.StrategyFactory;

/**
 * @ClassName StrategyFacade
 * @Description 最终只暴露我们的门面，对于里面的这些所有的工厂、枚举、策略等，都不暴露
 * 门面就是我们的超强封装
 * @Author gangye
 * @Date 2022/11/21
 */
public class StrategyFacade {
    // 可以定义一个map，不使用getStrategyEnum的switch方式

    public static Boolean pay(PayBody payBody){
        // 获取策略枚举
        StrategyEnum strategyEnum = getStrategyEnum(payBody.getType());
        if (strategyEnum == null) {
            return false;
        }
        // 获取策略对象
        PayStrategy payStrategy = StrategyFactory.getPayStrategy(strategyEnum);
        // 生成策略的上下文
        PayContext payContext = new PayContext(payStrategy);
        // 装饰一下context，立马多了一个功能
        AddFuncDecorator addFuncDecorator = new AddFuncDecorator(payContext);
        // 进行业务逻辑处理
        return addFuncDecorator.execute(payBody);
    }

    private static StrategyEnum getStrategyEnum(int type) {
        switch (type){
            case 0:
                return StrategyEnum.ZfbPayStrategy;
            case 1:
                return StrategyEnum.WxPayStrategy;
            case 2:
                return StrategyEnum.BkPayStrategy;
            default:
                return null;
        }
    }
}
