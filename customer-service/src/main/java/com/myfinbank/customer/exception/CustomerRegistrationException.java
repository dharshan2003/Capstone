package com.myfinbank.customer.exception;

public class CustomerRegistrationException extends RuntimeException {
    public CustomerRegistrationException(String message) {
        super(message);
    }
}
