package com.csrcb.design.handler;

import com.csrcb.design.pojo.UserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName SuggestBusinessHandlerProcess
 * @Description 抽象handler的执行类
 * @Author gangye
 * @Date 2022/11/27
 */
@Component
public class SuggestBusinessHandlerProcess {
    @Value("#{'${suggest.business.handler}'.split(',')}")
    private List<String> handlers;

    public void process(UserInfo userInfo, List<String> suggestLists){
        // 如果想要实时的进行顺序的调整或者是增减，那必须要使用配置中心进行配置
        // 比如springcloud里面自带的git这种方式配置； applo 配置中心。
        for (String handler : handlers){
            try {
                AbstractSuggestBusinessHandler handle = (AbstractSuggestBusinessHandler) Class.forName(handler).newInstance();
                handle.processHandler(userInfo, suggestLists);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
