package com.csrcb.design.pay.facade;

import com.csrcb.design.pay.additionalDecorator.AddFuncDecorator;
import com.csrcb.design.pay.factory.AddFuncFactory;
import com.csrcb.design.pay.factory.PayContextFactory;
import com.csrcb.design.pay.pojo.PayBody;
import com.csrcb.design.pay.strategy.PayStrategy;
import com.csrcb.design.pay.strategyContext.PayContext;
import com.csrcb.design.pay.strategyEnum.StrategyEnum;
import com.csrcb.design.pay.factory.StrategyFactory;

/**
 * @ClassName StrategyFacade
 * @Description 最终只暴露我们的门面，对于里面的这些所有的工厂、枚举、策略等，都不暴露
 * 门面就是我们的超强封装
 * @Author gangye
 * @Date 2022/11/21
 */
public class StrategyFacade {
    // 可以定义一个map，不使用getStrategyEnum的switch方式

    // 双十一的时候，有大量的用户进行下单（千万级），就会造成千万级的pay接口调用
    // 可惜，此处代码有两个new关键字 （new PayContext(payStrategy) 和 new AddFuncDecorator(payContext)）
    // 如果瞬时间有几十万的并发进来，那么会创建几十万的context对象和addFun对象，造成 年轻代的eden区的频繁对象创建
    // 虽然说调用完了就进行了对象的垃圾回收，但是这么多的访问对象进来就会造成 minorGC
    // 1. 单例模式？Paycontext创建是基于payStrategy，不止一种，单例不行，AddFuncDecorator是基于payContext创建的，因此它也是不止一种
    // 2. 享元模式。享元模式是单例模式的一种思想升级。单例模式，正对的是同一种对象，没有人格不同的细节；享元模式，正对多个对象。
    // 多个对象：同一种class，但是里面的属性有些许不同。PayContext是同一种帝乡，PayContext在细节上有不同(payStrategy)
    // 是否可以知道PayContext的种类的数量呢，目前有三种。享元模式能够对这可控数量的有不同细节的同一种Class进行共享，保证我们的程序不频繁的创建对象
    public static Boolean pay(PayBody payBody){
        // 获取策略枚举
        StrategyEnum strategyEnum = getStrategyEnum(payBody.getType());
        if (strategyEnum == null) {
            return false;
        }
        // 获取策略对象
        PayStrategy payStrategy = StrategyFactory.getPayStrategy(strategyEnum);
        // 生成策略的上下文
        PayContext payContext = (PayContext) PayContextFactory.getPayContext(payStrategy);
        // 装饰一下context，立马多了一个功能
        AddFuncDecorator addFuncDecorator = (AddFuncDecorator) AddFuncFactory.getAddFunc(payContext);
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
