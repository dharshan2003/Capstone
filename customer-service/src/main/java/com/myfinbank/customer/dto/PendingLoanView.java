package com.myfinbank.customer.dto;

public class PendingLoanView {

    private Long id;
    private String customerName;
    private double amount;
    private String type;

    public PendingLoanView() {
    }

    public PendingLoanView(Long id, String customerName, double amount, String type) {
        this.id = id;
        this.customerName = customerName;
        this.amount = amount;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
