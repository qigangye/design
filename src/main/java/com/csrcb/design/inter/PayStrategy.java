package com.csrcb.design.inter;

import com.csrcb.design.pojo.PayBody;

/**
 * @Classname PayStrategy
 * @Date 2022/9/25 21:47
 * @Created by gangye
 */
public interface PayStrategy {
    Boolean pay(PayBody payBody);
}
