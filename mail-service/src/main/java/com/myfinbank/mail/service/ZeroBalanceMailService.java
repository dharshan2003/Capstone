package com.myfinbank.mail.service;

import com.myfinbank.mail.dto.ZeroBalanceAlertRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ZeroBalanceMailService {

    private final JavaMailSender mailSender;

    @Value("${mail.admin.to}")
    private String adminTo;

    public ZeroBalanceMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendZeroBalanceAlert(ZeroBalanceAlertRequest req) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(adminTo);
        msg.setSubject("Zero Balance Alert - " + req.getAccountNumber());

        String text =
                "Customer: " + req.getCustomerName() + " (ID: " + req.getCustomerId() + ")\n" +
                "Account: " + req.getAccountNumber() + "\n" +
                "Balance: " + req.getBalance() + "\n\n" +
                "This account has reached zero balance.";

        msg.setText(text);
        mailSender.send(msg);
    }
}
