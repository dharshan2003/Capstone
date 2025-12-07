package com.myfinbank.customer.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionView {

    private Long transactionId;
    private String type;
    private BigDecimal amount;
    private String accountNumber; 
    private LocalDateTime timestamp;

    public Long getTransactionId() {
        return transactionId;
    }
    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    public String getAccountNumber() {          // ← getter
        return accountNumber;
    }
    public void setAccountNumber(String accountNumber) {   // ← setter
        this.accountNumber = accountNumber;
    }
}
