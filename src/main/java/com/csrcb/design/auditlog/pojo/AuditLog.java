package com.csrcb.design.auditlog.pojo;

import java.util.Date;

public class AuditLog {
    private String account;
    private String action;
    private Date date;
    private String orderId;
    private Object details; //订单创建需要有相关产品的信息；订单支付需要有相关产品信息以及支付方式和支付金额

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "AuditLog{" +
                "account='" + account + '\'' +
                ", action='" + action + '\'' +
                ", date=" + date +
                ", orderId='" + orderId + '\'' +
                ", details=" + details +
                '}';
    }
}
