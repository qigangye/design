package com.csrcb.design.pay.strategyContext;

import com.csrcb.design.pay.strategy.PayStrategy;
import com.csrcb.design.pay.pojo.PayBody;

/**
 * @Classname PayContext
 * @Date 2022/9/25 22:06
 * @Created by gangye
 */
// 装饰者模式：动态地给一个对象添加一些额外的职责    从抽象的根上添加
// PayContext 就是被装饰者，因为想给paycontext添加额外功能，平台币更新和红包等
// 所以选择使用装饰者模式，被装饰者必须要有 接口or抽象类
public class PayContext extends AbstractPayContext{
    private PayStrategy payStrategy;

    public PayContext(PayStrategy payStrategy) {
        this.payStrategy = payStrategy;
    }

    @Override
    public Boolean execute(PayBody payBody){
        return this.payStrategy.pay(payBody);
    }
}
