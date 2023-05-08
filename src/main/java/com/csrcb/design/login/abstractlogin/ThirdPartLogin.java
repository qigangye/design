package com.csrcb.design.login.abstractlogin;

import com.csrcb.design.login.implementor.LoginFunc;

public class ThirdPartLogin extends AbstractLoginProcessor{
    public ThirdPartLogin(LoginFunc loginFunc) {
        super(loginFunc);
    }

    @Override
    public boolean loginExecute(String name, String pwd, String type) {
        return super.loginFunc.login(name, pwd, type);
    }
}
