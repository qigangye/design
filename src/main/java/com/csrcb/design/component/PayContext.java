package com.csrcb.design.component;

import com.csrcb.design.pay.strategy.PayStrategy;
import com.csrcb.design.pay.pojo.PayBody;

/**
 * @Classname PayContext
 * @Date 2022/9/25 22:06
 * @Created by gangye
 */
public class PayContext {
    private PayStrategy payStrategy;

    public PayContext(PayStrategy payStrategy) {
        this.payStrategy = payStrategy;
    }

    public Boolean execute(PayBody payBody){
        return this.payStrategy.pay(payBody);
    }
}
