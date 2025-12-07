package com.myfinbank.customer.dto;

public class ZeroBalanceAlertView {

    private Long customerId;
    private String customerName;
    private double balance;

    public ZeroBalanceAlertView() {
    }

    public ZeroBalanceAlertView(Long customerId, String customerName, double balance) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.balance = balance;
    }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
}
