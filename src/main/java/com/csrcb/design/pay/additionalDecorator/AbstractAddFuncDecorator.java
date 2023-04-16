package com.csrcb.design.pay.additionalDecorator;

import com.csrcb.design.pay.pojo.PayBody;
import com.csrcb.design.pay.strategyContext.AbstractPayContext;

public abstract class AbstractAddFuncDecorator extends AbstractPayContext {
    // 这是装饰器类，专门添加新功能的（平台币、红包等其他业务）
    private AbstractPayContext abstractPayContext = null;

    public AbstractAddFuncDecorator(AbstractPayContext abstractPayContext) {
        this.abstractPayContext = abstractPayContext;
    }

    // 装饰器开始干活
    // 1. 做本来应该支持的事儿。支付，但是不能修改支付代码，也不能修改支付逻辑
    @Override
    public Boolean execute(PayBody payBody) {
        return abstractPayContext.execute(payBody);
    }

    // 2. 做点额外的功能
    public abstract void addtionalFunction(PayBody payBody);
}
