package com.myfinbank.customer.dto;

public class CustomerLoginRequest {

    private String loginName;
    private String loginSecret;

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginSecret() {
        return loginSecret;
    }

    public void setLoginSecret(String loginSecret) {
        this.loginSecret = loginSecret;
    }
}
