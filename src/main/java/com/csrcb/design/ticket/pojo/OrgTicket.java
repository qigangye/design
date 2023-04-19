package com.csrcb.design.ticket.pojo;

public class OrgTicket {
    private String type;
    private String footer;

    private String title;
    private String account;

    private String content;//从配置中心或者db。（缓存里获取，只不过如果一放DB或者配置中心有修改，需要同时更新缓存）
    private String product;//db中获取（缓存里有一些热门产品的）
    private String bankInfo;//校验我们的银行卡信息(通过我们的第三方银行相关接口进行的校验)

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
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

    public String getBankInfo() {
        return bankInfo;
    }

    public void setBankInfo(String bankInfo) {
        this.bankInfo = bankInfo;
    }
}
