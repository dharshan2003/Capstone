package com.myfinbank.customer.dto;

import java.math.BigDecimal;

public class FixedDepositCreateRequest {

    private Long customerId;
    private String accountNumber;
    private BigDecimal principalAmount;
    private Integer months;
    private BigDecimal interestRate;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public Integer getMonths() { return months; }
    public void setMonths(Integer months) { this.months = months; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
}
