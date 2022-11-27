package com.csrcb.design.handler;

import com.csrcb.design.pojo.UserInfo;

import java.util.List;

/**
 * @ClassName AbstractSuggestBusinessHandler
 * @Description 抽象投放业务handler类
 * @Author gangye
 * @Date 2022/11/27
 */
public abstract class AbstractSuggestBusinessHandler {
    /**
     * @param userInfo 用户信息
     * @param suggestLists 当前用户需要被投放的业务
     */
    abstract void processHandler(UserInfo userInfo, List<String> suggestLists);
}
