package com.csrcb.design.component;

import com.csrcb.design.pojo.PayBody;
import org.springframework.stereotype.Component;

/**
 * @Classname PayHandler
 * @Date 2022/9/25 21:31
 * @Created by gangye
 */
@Component
public class PayHandler {
    public boolean zfbPay(PayBody payBody) {
        // 详细的调用支付宝的逻辑。支付宝提供的第三方接口
        return true;
    }

    public boolean wxPay(PayBody payBody) {
        return true;
    }

    public boolean bkPay(PayBody payBody) {
        return true;
    }
}
