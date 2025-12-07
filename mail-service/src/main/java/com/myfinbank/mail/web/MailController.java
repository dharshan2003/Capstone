package com.myfinbank.mail.web;

import com.myfinbank.mail.dto.ZeroBalanceAlertRequest;
import com.myfinbank.mail.service.ZeroBalanceMailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/notifications")
public class MailController {

    private final ZeroBalanceMailService mailService;

    public MailController(ZeroBalanceMailService mailService) {
        this.mailService = mailService;
    }

    @PostMapping("/zero-balance")
    public ResponseEntity<Void> sendZeroBalance(@RequestBody ZeroBalanceAlertRequest req) {
        mailService.sendZeroBalanceAlert(req);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }
}
