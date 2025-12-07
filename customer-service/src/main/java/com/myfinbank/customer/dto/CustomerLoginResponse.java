package com.myfinbank.customer.dto;

public class CustomerLoginResponse {

    private String token;
    private Long customerId;
    private String displayName;

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
}
