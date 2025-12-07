package com.myfinbank.customer.service;

import java.util.List;
import java.util.stream.Collectors;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.myfinbank.customer.dto.ZeroBalanceAlertView;
import com.myfinbank.customer.model.BankAccount;
import com.myfinbank.customer.repository.BankAccountRepository;

@Service
public class AlertService {

    private final BankAccountRepository accountRepo;

    public AlertService(BankAccountRepository accountRepo) {
        this.accountRepo = accountRepo;
    }

    public List<ZeroBalanceAlertView> getZeroBalanceAlerts() {
        return accountRepo.findByBalance(BigDecimal.ZERO)
                .stream()
                .map(acc -> new ZeroBalanceAlertView(
                        acc.getOwner().getId(),          // customerId
                        acc.getOwner().getFullName(),    // customerName
                        acc.getBalance().doubleValue())) // balance
                .collect(Collectors.toList());
    }

}
