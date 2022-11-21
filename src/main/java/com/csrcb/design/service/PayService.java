package com.csrcb.design.service;

import com.csrcb.design.pay.facade.StrategyFacade;
import com.csrcb.design.pay.strategyContext.PayContext;
import com.csrcb.design.pay.strategyEnum.StrategyEnum;
import com.csrcb.design.pay.strategyFactory.StrategyFactory;
import com.csrcb.design.pay.pojo.PayBody;
import org.springframework.stereotype.Service;

/**
 * @Classname PayService
 * @Date 2022/9/25 21:18
 * @Created by gangye
 */
@Service
public class PayService {

    // 后续对于付款模块的删除或增加或修改，无需改动service
    // 不会对调用层产生任何代码的改动
    // 调用层使用pay模块，无需关系实现的逻辑，只需要将入参传递给pay模块即可
    public Boolean pay(PayBody payBody){
        // 书写付款逻辑
        boolean flag = false;
        flag = StrategyFacade.pay(payBody);
        if (flag) {
            // 如果是true，保存到db
            saveToDb(payBody);
        }
        return flag;
    }

    private void saveToDb(PayBody payBody) {
    }
}
