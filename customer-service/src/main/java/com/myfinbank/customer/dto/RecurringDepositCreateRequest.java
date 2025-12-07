package com.myfinbank.customer.dto;

import java.math.BigDecimal;

public class RecurringDepositCreateRequest {

    private Long customerId;
    private String accountNumber;
    private BigDecimal monthlyInstallment;
    private Integer months;
    private BigDecimal interestRate;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public BigDecimal getMonthlyInstallment() { return monthlyInstallment; }
    public void setMonthlyInstallment(BigDecimal monthlyInstallment) { this.monthlyInstallment = monthlyInstallment; }

    public Integer getMonths() { return months; }
    public void setMonths(Integer months) { this.months = months; }

    public BigDecimal getInterestRate() { return interestRate; }
    public void setInterestRate(BigDecimal interestRate) { this.interestRate = interestRate; }
}
