package com.csrcb.design.ticket.builder;

import com.csrcb.design.ticket.TicketConstant;
import com.csrcb.design.ticket.pojo.OrgTicket;

public class OrgTicketBuilder extends AbstractTicketBuilder<OrgTicket> {
    private OrgTicket orgTicket = TicketConstant.orgTicket.clone();  // new 关键字  改成clone模式，只clone我们的不可变部分，对于可变部分和自定义用户提交部分，不进行clone

    @Override
    public void setBankInfo(String bankInfo) {
        orgTicket.setBankInfo(bankInfo);
    }

//    @Override
//    public void setCommon(String type, String footer) {
//        orgTicket.setType(type);
//        orgTicket.setFooter(footer);
//    }

    @Override
    public void setParam(String account, String title) {
        orgTicket.setAccount(account);
        orgTicket.setTitle(title);
    }

    @Override
    public void setContent(String content) {
        orgTicket.setContent(content);
    }

    @Override
    public void setProduct(String product) {
        orgTicket.setProduct(product);
    }

    @Override
    public OrgTicket buildTicket() {
        return orgTicket;
    }
}
