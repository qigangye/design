package com.csrcb.design.ticket;

import com.csrcb.design.ticket.pojo.OrgTicket;
import com.csrcb.design.ticket.pojo.PsnTicket;

public class TicketConstant {
    // 保存只具有公共属性的队形，供clone使用
    public static PsnTicket psnTicket = new PsnTicket();
    public static OrgTicket orgTicket = new OrgTicket();

    static {
        psnTicket.setType("type");
        psnTicket.setFooter("footer");
        orgTicket.setType("type");
        orgTicket.setFooter("footer");
    }
}
