package com.csrcb.design.pay.strategy;

import com.csrcb.design.pay.pojo.PayBody;

/**
 * @Classname WxPayStrategy
 * @Date 2022/9/25 21:48
 * @Created by gangye
 */
public class WxPayStrategy implements PayStrategy {
    public Boolean pay(PayBody payBody) {
        // 支付细节逻辑。。。
        return true;
    }
}
