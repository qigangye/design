package com.csrcb.design.handler;

import com.csrcb.design.pojo.UserInfo;

import java.util.List;

/**
 * @ClassName PersonalCheckHandler
 * @Description 个人资质检验的handler
 * @Author gangye
 * @Date 2022/11/27
 */
public class PersonalCheckHandler extends AbstractSuggestBusinessHandler{
    @Override
    public void processHandler(UserInfo userInfo, List<String> suggestLists) {
        // 模拟：通过个人资质的check，我们找到了4个可以投放的业务，放到 suggestLists 中。
        suggestLists.add("1");
        suggestLists.add("2");
        suggestLists.add("3");
        suggestLists.add("4");
    }
}
