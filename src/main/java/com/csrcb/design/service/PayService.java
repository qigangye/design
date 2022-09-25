package com.csrcb.design.service;

import com.csrcb.design.component.PayHandler;
import com.csrcb.design.pojo.PayBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Classname PayService
 * @Date 2022/9/25 21:18
 * @Created by gangye
 */
@Service
public class PayService {
    @Autowired
    private PayHandler payHandler;

    public Boolean pay(PayBody payBody){
        // 书写付款逻辑
        boolean flag = false;
        if (payBody.getType() == 0){
            // 支付宝
            flag = payHandler.zfbPay(payBody);
        } else if (payBody.getType() == 1){
            // wechat
            flag = payHandler.wxPay(payBody);
        } else if (payBody.getType() == 2){
            // bank
            flag = payHandler.bkPay(payBody);
        } else {
            throw new UnsupportedOperationException("Unsupport type, please choose 0,1,2");
        }
        if (flag) {
            // 如果是true，保存到db
            saveToDb(payBody);
        }
        return flag;
    }

    private void saveToDb(PayBody payBody) {
    }
}
