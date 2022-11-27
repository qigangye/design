package com.csrcb.design.pojo;

import java.util.List;

/**
 * @ClassName UserInfo
 * @Description 用户的pojo
 * @Author gangye
 * @Date 2022/11/27
 */
public class UserInfo {
    private String username;
    private String city;
    private List<String> buyProducts;
    private boolean isNewUser;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public List<String> getBuyProducts() {
        return buyProducts;
    }

    public void setBuyProducts(List<String> buyProducts) {
        this.buyProducts = buyProducts;
    }

    public boolean isNewUser() {
        return isNewUser;
    }

    public void setNewUser(boolean newUser) {
        isNewUser = newUser;
    }
}
