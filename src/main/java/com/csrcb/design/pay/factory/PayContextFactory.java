package com.csrcb.design.pay.factory;

import com.csrcb.design.pay.strategy.PayStrategy;
import com.csrcb.design.pay.strategyContext.AbstractPayContext;
import com.csrcb.design.pay.strategyContext.PayContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PayContextFactory {
    public final static Map<PayStrategy, PayContext> maps = new ConcurrentHashMap<>();

    public static AbstractPayContext getPayContext(PayStrategy payStrategy){
        if (maps.get(payStrategy) == null) {
            PayContext payContext = new PayContext(payStrategy);
            maps.put(payStrategy, payContext);
        }
        return maps.get(payStrategy);
    }
}
