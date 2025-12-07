package com.myfinbank.customer.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myfinbank.customer.dto.FixedDepositCreateRequest;
import com.myfinbank.customer.model.BankAccount;
import com.myfinbank.customer.model.CustomerProfile;
import com.myfinbank.customer.model.FixedDeposit;
import com.myfinbank.customer.repository.BankAccountRepository;
import com.myfinbank.customer.repository.CustomerProfileRepository;
import com.myfinbank.customer.repository.FixedDepositRepository;

@Service
public class FixedDepositService {

    private final CustomerProfileRepository customerRepo;
    private final BankAccountRepository accountRepo;
    private final FixedDepositRepository fdRepo;

    public FixedDepositService(CustomerProfileRepository customerRepo,
                               BankAccountRepository accountRepo,
                               FixedDepositRepository fdRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.fdRepo = fdRepo;
    }

    @Transactional
    public FixedDeposit createFixedDeposit(FixedDepositCreateRequest request) {

        CustomerProfile customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (request.getPrincipalAmount() == null ||
                request.getPrincipalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Principal amount must be positive");
        }
        if (request.getMonths() == null || request.getMonths() <= 0) {
            throw new IllegalArgumentException("Months must be positive");
        }
        if (request.getInterestRate() == null ||
                request.getInterestRate().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Interest rate must be positive");
        }

        BankAccount account = accountRepo.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalStateException("Only ACTIVE accounts can be linked to FD");
        }
        if (account.getBalance().compareTo(request.getPrincipalAmount()) < 0) {
            throw new IllegalStateException("Insufficient balance to create FD");
        }

        // deduct from main account
        account.setBalance(account.getBalance().subtract(request.getPrincipalAmount()));
        accountRepo.save(account);

        BigDecimal maturity = calculateMaturityAmount(
                request.getPrincipalAmount(),
                request.getInterestRate(),
                request.getMonths()
        );

        FixedDeposit fd = new FixedDeposit();
        fd.setCustomer(customer);
        fd.setLinkedAccountNumber(request.getAccountNumber());
        fd.setPrincipalAmount(request.getPrincipalAmount());
        fd.setMonths(request.getMonths());
        fd.setInterestRate(request.getInterestRate());
        fd.setMaturityAmount(maturity);
        fd.setStatus("ACTIVE");

        return fdRepo.save(fd);
    }

    public List<FixedDeposit> getFDsForCustomer(Long customerId) {
        return fdRepo.findByCustomerIdOrderByIdDesc(customerId);
    }

    // very simple interest: P * (1 + r * n/12)
    private BigDecimal calculateMaturityAmount(BigDecimal principal,
                                               BigDecimal annualRate,
                                               int months) {
        BigDecimal years = BigDecimal.valueOf(months)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
        BigDecimal rateAsDecimal = annualRate
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal factor = BigDecimal.ONE.add(rateAsDecimal.multiply(years));
        return principal.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}
