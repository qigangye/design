package com.csrcb.design.pay.strategyContext;

import com.csrcb.design.pay.pojo.PayBody;

public abstract class AbstractPayContext {
    public abstract Boolean execute(PayBody payBody);
}
