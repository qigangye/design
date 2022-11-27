package com.csrcb.design.handler;

import com.csrcb.design.pojo.UserInfo;

import java.util.List;

/**
 * @ClassName CityCheckHandler
 * @Description 近期购买校验的handler
 * @Author gangye
 * @Date 2022/11/27
 */
public class RecentCheckHandler extends AbstractSuggestBusinessHandler{
    @Override
    public void processHandler(UserInfo userInfo, List<String> suggestLists) {
        // 通过 userInfo 获取 近期购买的产品信息buyProducts 属性
        List<String> buyProducts = userInfo.getBuyProducts();
        // 通过 buyProducts 和之前保留的 3 个业务信息进行对比，然后筛选出剩余的 2 个业务投放
        suggestLists.remove("2");
    }
}
