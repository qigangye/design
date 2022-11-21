package com.csrcb.design.pay.strategy;

import com.csrcb.design.pojo.PayBody;

/**
 * @Classname BkPayStrategy
 * @Date 2022/9/25 21:48
 * @Created by gangye
 */
public class BkPayStrategy implements PayStrategy {
    public Boolean pay(PayBody payBody) {
        // 支付细节逻辑。。。
        return true;
    }
}
