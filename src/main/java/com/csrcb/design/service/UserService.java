package com.csrcb.design.service;

import com.csrcb.design.handler.SuggestBusinessHandlerProcess;
import com.csrcb.design.pojo.TicketParam;
import com.csrcb.design.pojo.UserInfo;
import com.csrcb.design.ticket.builder.AbstractTicketBuilder;
import com.csrcb.design.ticket.builder.OrgTicketBuilder;
import com.csrcb.design.ticket.builder.PsnTicketBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName UserService
 * @Description 用户的业务逻辑service类
 * @Author gangye
 * @Date 2022/11/27
 */
@Service
public class UserService {
    @Autowired
    private SuggestBusinessHandlerProcess suggestBusinessHandlerProcess;

    public List<String> suggestBusiness(String username) {
        // 获取用户信息。因为用户已经登录了，用户的信息保存在我们的缓存里的。
        UserInfo userInfo = getUserInfo(username);
        List<String> res = new ArrayList<>();
        // 此时，调用方无需关心任何handler。完全对其屏蔽，而且是完全解耦
        suggestBusinessHandlerProcess.process(userInfo, res);
        return res;
    }

    //因为这部分需要查询 缓存 （如果缓存没有，需要查库），不应该卸载service层，为了代码书写和案例展示，简单模拟在service中
    private UserInfo getUserInfo(String username) {
        return new UserInfo();
    }

    public Object getTicket(TicketParam ticketParam) {
        AbstractTicketBuilder builder = null;
        String bankInfo = null;
        if (null == ticketParam.getBankInfo()) {
            bankInfo = "check information from other channel interface";
            builder = new PsnTicketBuilder();
        } else {
            builder = new OrgTicketBuilder();
        }
        builder.setCommon(ticketParam.getType(), ticketParam.getFooter());
        builder.setParam(ticketParam.getAccount(), ticketParam.getTitle());
        String content = "from config center";
        String product = "from db";
        builder.setContent(content);
        builder.setProduct(product);
        builder.setBankInfo(bankInfo);
        // 详细的逻辑细节控制以及从配置中心或者db获取逻辑步骤就是简单的crud业务处理，自己明白即可
        return builder.buildTicket();
    }
}
