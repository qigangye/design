package com.csrcb.design.inter;

import com.csrcb.design.pojo.PayBody;

/**
 * @Classname ZfbPayStrategy
 * @Date 2022/9/25 21:48
 * @Created by gangye
 */
public class ZfbPayStrategy implements PayStrategy {
    public Boolean pay(PayBody payBody) {
        // 支付细节逻辑。。。
        return true;
    }
}