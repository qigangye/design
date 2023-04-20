package com.csrcb.design.ticket.builder;

public abstract class AbstractTicketBuilder<T> {
//    public abstract void setCommon(String type, String footer);

    public abstract void setParam(String account, String title);

    public abstract void setContent(String content);// 配置中心或DB或缓存

    public abstract void setProduct(String product);// db or 热数据缓存

    public void setBankInfo(String bankInfo){} //需要校验的，校验通过才能设置

    public abstract T buildTicket();
}
