package com.csrcb.design.pay.additionalDecorator;

import com.csrcb.design.pay.pojo.PayBody;
import com.csrcb.design.pay.strategyContext.AbstractPayContext;

// abstractPayContext不是共享的，因为不同的客户选择的支付方式是不一样的
public class AddFuncDecorator extends AbstractAddFuncDecorator{
    public AddFuncDecorator(AbstractPayContext abstractPayContext) {
        super(abstractPayContext);
    }

    @Override
    public void addtionalFunction(PayBody payBody) {// 共享的，与支付策略没有任何关系
        String product = payBody.getProduct();
        // 从db中获取product的详细信息
        // 从配置中心(redis缓存)中获取产品的更新策略
        // 根据策略更新用户平台币 或 发放红包
        System.out.println("更新平台币成功，发送红包到用户优惠券模块成功");
    }

    // 新活儿、老活儿的逻辑组装
    @Override
    public Boolean execute(PayBody payBody) {// 算共享的，共享的调用逻辑
        boolean res = super.execute(payBody); // 老活儿
        this.addtionalFunction(payBody); // 新活儿，所有的新活儿的各种重试，失败补偿都在这一行处理
        return res;
    }
}
