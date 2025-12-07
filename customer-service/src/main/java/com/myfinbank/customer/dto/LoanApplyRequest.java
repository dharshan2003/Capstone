package com.myfinbank.customer.dto;

import java.math.BigDecimal;

public class LoanApplyRequest {

    private Long customerId;
    private BigDecimal principalAmount;
    private BigDecimal annualRate;
    private Integer months;

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public BigDecimal getAnnualRate() { return annualRate; }
    public void setAnnualRate(BigDecimal annualRate) { this.annualRate = annualRate; }

    public Integer getMonths() { return months; }
    public void setMonths(Integer months) { this.months = months; }
}
