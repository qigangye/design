package com.csrcb.design.ticket.pojo;

public class PsnTicket implements Cloneable{
    private String type;
    private String footer;

    private String title;
    private String account;

    private String content;//从配置中心或者db。（缓存里获取，只不过如果一放DB或者配置中心有修改，需要同时更新缓存）
    private String product;//db中获取（缓存里有一些热门产品的）

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    @Override
    public PsnTicket clone() {
        PsnTicket psnTicket = null;
        try {
            psnTicket = (PsnTicket) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
        return psnTicket;
    }
}
