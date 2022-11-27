package com.csrcb.design.handler;

import com.csrcb.design.pojo.UserInfo;

import java.util.List;

/**
 * @ClassName CityCheckHandler
 * @Description 城市校验的handler
 * @Author gangye
 * @Date 2022/11/27
 */
public class CityCheckHandler extends AbstractSuggestBusinessHandler{
    @Override
    public void processHandler(UserInfo userInfo, List<String> suggestLists) {
        // 通过userInfo 获取city属性
        String city = userInfo.getCity();
        // 通过 city 和之前保留的 4 个业务信息进行对比，然后筛选出剩余的 3 个业务投放
        suggestLists.remove("1");
    }
}
