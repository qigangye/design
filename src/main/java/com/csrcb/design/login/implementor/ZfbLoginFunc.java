package com.csrcb.design.login.implementor;

public class ZfbLoginFunc implements LoginFunc{
    @Override
    public boolean login(String name, String pwd, String type) {
        // 进行第三方账号的校验流程
        System.out.println("支付宝验证通过，可以登录");
        return true;
    }
}
