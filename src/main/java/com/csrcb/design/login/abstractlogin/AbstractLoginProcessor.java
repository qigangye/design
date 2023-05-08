package com.csrcb.design.login.abstractlogin;

import com.csrcb.design.login.implementor.LoginFunc;

public abstract class AbstractLoginProcessor {
    protected LoginFunc loginFunc;

    public AbstractLoginProcessor(LoginFunc loginFunc) {
        this.loginFunc = loginFunc;
    }

    public abstract boolean loginExecute(String name, String pwd, String type);
}
