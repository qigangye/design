package com.csrcb.design.pay.factory;

import com.csrcb.design.pay.additionalDecorator.AbstractAddFuncDecorator;
import com.csrcb.design.pay.additionalDecorator.AddFuncDecorator;
import com.csrcb.design.pay.strategyContext.PayContext;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddFuncFactory {
    // 工厂目的——生产AddFuncDecorator；「生产多个AddFuncDecorator->用来享元」
    // key 应该是能够和AbstractAddFuncDecorator 做成对应的东西
    // 由于策略只有三种，这个map最多保存三个对象。
    public final static Map<PayContext, AbstractAddFuncDecorator> maps = new ConcurrentHashMap<>();

    public static AbstractAddFuncDecorator getAddFunc(PayContext payContext){
        if (maps.get(payContext) == null) {
            AddFuncDecorator addFuncDecorator = new AddFuncDecorator(payContext);
            maps.put(payContext, addFuncDecorator);
        }
        return maps.get(payContext);
    }
}
