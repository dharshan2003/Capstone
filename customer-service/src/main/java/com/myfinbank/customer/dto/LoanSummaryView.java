package com.myfinbank.customer.dto;

import java.math.BigDecimal;

public class LoanSummaryView {

    private Long id;
    private BigDecimal principalAmount;
    private BigDecimal annualRate;
    private Integer months;
    private BigDecimal emiAmount;
    private String status;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public BigDecimal getPrincipalAmount() { return principalAmount; }
    public void setPrincipalAmount(BigDecimal principalAmount) { this.principalAmount = principalAmount; }

    public BigDecimal getAnnualRate() { return annualRate; }
    public void setAnnualRate(BigDecimal annualRate) { this.annualRate = annualRate; }

    public Integer getMonths() { return months; }
    public void setMonths(Integer months) { this.months = months; }

    public BigDecimal getEmiAmount() { return emiAmount; }
    public void setEmiAmount(BigDecimal emiAmount) { this.emiAmount = emiAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
