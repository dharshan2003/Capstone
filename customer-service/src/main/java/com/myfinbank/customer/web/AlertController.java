package com.myfinbank.customer.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.myfinbank.customer.dto.ZeroBalanceAlertView;
import com.myfinbank.customer.service.AlertService;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/zero-balance")
    public List<ZeroBalanceAlertView> getZeroBalanceAlerts() {
        return alertService.getZeroBalanceAlerts();
    }
}
