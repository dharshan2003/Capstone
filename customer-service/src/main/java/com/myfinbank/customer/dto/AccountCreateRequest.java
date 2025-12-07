package com.myfinbank.customer.dto;

import java.math.BigDecimal;

public class AccountCreateRequest {

    private Long customerId;
    private String accountType;
    private BigDecimal openingBalance;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getAccountType() { return accountType; }
    public void setAccountType(String accountType) { this.accountType = accountType; }

    public BigDecimal getOpeningBalance() { return openingBalance; }
    public void setOpeningBalance(BigDecimal openingBalance) { this.openingBalance = openingBalance; }
}
