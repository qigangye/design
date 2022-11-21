package com.csrcb.design.pay.strategy;

import com.csrcb.design.pay.pojo.PayBody;

/**
 * @Classname PayStrategy
 * @Date 2022/9/25 21:47
 * @Created by gangye
 */
public interface PayStrategy {
    Boolean pay(PayBody payBody);
}
