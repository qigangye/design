package com.csrcb.design.pojo;

/**
 * @Classname PayBody
 * @Date 2022/9/25 21:20
 * @Created by gangye
 */
public class PayBody {
    private String account;
    private int type;
    private String product;
    private int amount;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
