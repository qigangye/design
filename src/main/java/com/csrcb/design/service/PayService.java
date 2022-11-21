package com.csrcb.design.service;

import com.csrcb.design.pay.strategyContext.PayContext;
import com.csrcb.design.pay.strategyEnum.StrategyEnum;
import com.csrcb.design.pay.strategyFactory.StrategyFactory;
import com.csrcb.design.pojo.PayBody;
import org.springframework.stereotype.Service;

/**
 * @Classname PayService
 * @Date 2022/9/25 21:18
 * @Created by gangye
 */
@Service
public class PayService {

    public Boolean pay(PayBody payBody){
        // 书写付款逻辑
        boolean flag = false;
        if (payBody.getType() == 0){
            // 支付宝
//            flag = payHandler.zfbPay(payBody);
            flag = new PayContext(StrategyFactory.getPayStrategy(StrategyEnum.ZfbPayStrategy)).execute(payBody);
        } else if (payBody.getType() == 1){
            // wechat
            flag = new PayContext(StrategyFactory.getPayStrategy(StrategyEnum.WxPayStrategy)).execute(payBody);
        } else if (payBody.getType() == 2){
            // bank
            flag = new PayContext(StrategyFactory.getPayStrategy(StrategyEnum.BkPayStrategy)).execute(payBody);
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
