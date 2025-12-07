package com.myfinbank.customer.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myfinbank.customer.dto.RecurringDepositCreateRequest;
import com.myfinbank.customer.model.BankAccount;
import com.myfinbank.customer.model.CustomerProfile;
import com.myfinbank.customer.model.RecurringDeposit;
import com.myfinbank.customer.repository.BankAccountRepository;
import com.myfinbank.customer.repository.CustomerProfileRepository;
import com.myfinbank.customer.repository.RecurringDepositRepository;

@Service
public class RecurringDepositService {

    private final CustomerProfileRepository customerRepo;
    private final BankAccountRepository accountRepo;
    private final RecurringDepositRepository rdRepo;

    public RecurringDepositService(CustomerProfileRepository customerRepo,
                                   BankAccountRepository accountRepo,
                                   RecurringDepositRepository rdRepo) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.rdRepo = rdRepo;
    }

    @Transactional
    public RecurringDeposit createRecurringDeposit(RecurringDepositCreateRequest request) {

        CustomerProfile customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (request.getMonthlyInstallment() == null ||
                request.getMonthlyInstallment().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Monthly installment must be positive");
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
            throw new IllegalStateException("Only ACTIVE accounts can be linked to RD");
        }
        if (account.getBalance().compareTo(request.getMonthlyInstallment()) < 0) {
            throw new IllegalStateException("Insufficient balance to start RD");
        }

        // deduct first installment now
        account.setBalance(account.getBalance().subtract(request.getMonthlyInstallment()));
        accountRepo.save(account);

        BigDecimal maturity = calculateMaturityAmount(
                request.getMonthlyInstallment(),
                request.getInterestRate(),
                request.getMonths()
        );

        RecurringDeposit rd = new RecurringDeposit();
        rd.setCustomer(customer);
        rd.setLinkedAccountNumber(request.getAccountNumber());
        rd.setMonthlyInstallment(request.getMonthlyInstallment());
        rd.setMonths(request.getMonths());
        rd.setInterestRate(request.getInterestRate());
        rd.setMaturityAmount(maturity);
        rd.setStatus("ACTIVE");

        return rdRepo.save(rd);
    }

    public List<RecurringDeposit> getRDsForCustomer(Long customerId) {
        return rdRepo.findByCustomerIdOrderByIdDesc(customerId);
    }

    // very simple approximation of RD maturity:
    // totalPrincipal = monthly * months
    // maturity = totalPrincipal * (1 + r * n/12)
    private BigDecimal calculateMaturityAmount(BigDecimal monthly,
                                               BigDecimal annualRate,
                                               int months) {
        BigDecimal totalPrincipal = monthly.multiply(BigDecimal.valueOf(months));
        BigDecimal years = BigDecimal.valueOf(months)
                .divide(BigDecimal.valueOf(12), 4, RoundingMode.HALF_UP);
        BigDecimal rateAsDecimal = annualRate
                .divide(BigDecimal.valueOf(100), 6, RoundingMode.HALF_UP);
        BigDecimal factor = BigDecimal.ONE.add(rateAsDecimal.multiply(years));
        return totalPrincipal.multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }
}
