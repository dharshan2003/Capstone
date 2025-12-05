package com.myfinbank.admin.dto;

public class AdminSignupRequest {

    private String displayName;
    private String email;
    private String loginName;
    private String loginSecret;

    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getLoginName() { return loginName; }
    public void setLoginName(String loginName) { this.loginName = loginName; }

    public String getLoginSecret() { return loginSecret; }
    public void setLoginSecret(String loginSecret) { this.loginSecret = loginSecret; }
}
