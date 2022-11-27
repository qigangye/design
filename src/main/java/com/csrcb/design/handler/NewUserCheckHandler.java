package com.csrcb.design.handler;

import com.csrcb.design.pojo.UserInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName CityCheckHandler
 * @Description 是否新用户校验的handler
 * @Author gangye
 * @Date 2022/11/27
 */
public class NewUserCheckHandler extends AbstractSuggestBusinessHandler{
    @Override
    public void processHandler(UserInfo userInfo, List<String> suggestLists) {
        // 通过 userInfo 获取 新用户标志 isNewUser 属性
        boolean newUser = userInfo.isNewUser();
        if (newUser) {
            suggestLists = new ArrayList<>(); // 特定的新用户奖励
        }
    }
}
