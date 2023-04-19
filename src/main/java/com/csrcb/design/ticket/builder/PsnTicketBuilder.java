package com.csrcb.design.ticket.builder;

import com.csrcb.design.ticket.pojo.PsnTicket;

public class PsnTicketBuilder extends AbstractTicketBuilder<PsnTicket> {
    private PsnTicket psnTicket = new PsnTicket();
    @Override
    public void setCommon(String type, String footer) {
        psnTicket.setType(type);
        psnTicket.setFooter(footer);
    }

    @Override
    public void setParam(String account, String title) {
        psnTicket.setAccount(account);
        psnTicket.setTitle(title);
    }

    @Override
    public void setContent(String content) {
        psnTicket.setContent(content);
    }

    @Override
    public void setProduct(String product) {
        psnTicket.setProduct(product);
    }

    @Override
    public PsnTicket buildTicket() {
        return psnTicket;
    }
}
