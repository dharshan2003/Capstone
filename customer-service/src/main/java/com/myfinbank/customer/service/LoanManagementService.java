package com.myfinbank.customer.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.myfinbank.customer.dto.LoanApplyRequest;
import com.myfinbank.customer.dto.PendingLoanView;
import com.myfinbank.customer.model.BankAccount;
import com.myfinbank.customer.model.CustomerProfile;
import com.myfinbank.customer.model.LoanApplication;
import com.myfinbank.customer.repository.BankAccountRepository;
import com.myfinbank.customer.repository.CustomerProfileRepository;
import com.myfinbank.customer.repository.LoanApplicationRepository;

@Service
public class LoanManagementService {

    private static final BigDecimal LOAN_APPROVAL_THRESHOLD =
            BigDecimal.valueOf(10000);   // demo rule based on balance

    private final CustomerProfileRepository customerRepo;
    private final LoanApplicationRepository loanRepo;
    private final BankAccountRepository accountRepo;

    public LoanManagementService(CustomerProfileRepository customerRepo,
                                 LoanApplicationRepository loanRepo,
                                 BankAccountRepository accountRepo) {
        this.customerRepo = customerRepo;
        this.loanRepo = loanRepo;
        this.accountRepo = accountRepo;
    }

    @Transactional
    public LoanApplication applyForLoan(LoanApplyRequest request) {
        CustomerProfile customer = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        BigDecimal principal = request.getPrincipalAmount();
        BigDecimal annualRate = request.getAnnualRate();
        Integer months = request.getMonths();

        if (principal == null || principal.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Loan amount must be positive");
        }
        if (annualRate == null || annualRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Interest rate must be positive");
        }
        if (months == null || months <= 0) {
            throw new IllegalArgumentException("Months must be positive");
        }

        BigDecimal emi = calculateEmi(principal, annualRate, months);

        LoanApplication loan = new LoanApplication();
        loan.setCustomer(customer);
        loan.setPrincipalAmount(principal);
        loan.setAnnualRate(annualRate);
        loan.setMonths(months);
        loan.setEmiAmount(emi);
        loan.setStatus("APPLIED");

        return loanRepo.save(loan);
    }

    public List<LoanApplication> getAllLoans() {
        return loanRepo.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    @Transactional
    public LoanApplication decideLoan(Long loanId) {
        LoanApplication loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));

        CustomerProfile customer = loan.getCustomer();
        if (customer == null) {
            throw new IllegalStateException("Loan is not linked to a customer");
        }

        List<BankAccount> accounts = accountRepo.findByOwner(customer);
        BankAccount mainAccount = accounts.stream()
                .filter(a -> "ACTIVE".equalsIgnoreCase(a.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No active account found for customer"));

        BigDecimal balance = mainAccount.getBalance();
        String status = balance.compareTo(LOAN_APPROVAL_THRESHOLD) >= 0 ? "APPROVED" : "DENIED";

        loan.setStatus(status);
        return loanRepo.save(loan);
    }

    public List<LoanApplication> getLoansForCustomer(Long customerId) {
        return loanRepo.findByCustomerIdOrderByIdDesc(customerId);
    }

    // ---------- NEW methods for admin dashboard ----------

    // list only loans that are still waiting for a decision
    @Transactional(readOnly = true)
    public List<PendingLoanView> getPendingLoans() {
        return loanRepo.findByStatusOrderByIdDesc("APPLIED")
                .stream()
                .map(loan -> {
                    PendingLoanView view = new PendingLoanView();
                    view.setId(loan.getId());
                    view.setCustomerName(loan.getCustomer().getFullName());
                    view.setAmount(loan.getPrincipalAmount().doubleValue());
                    view.setType("STANDARD"); // or use a real field if you have one
                    return view;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void approveLoan(Long loanId) {
        LoanApplication loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loan.setStatus("APPROVED");
        loanRepo.save(loan);
    }

    @Transactional
    public void denyLoan(Long loanId) {
        LoanApplication loan = loanRepo.findById(loanId)
                .orElseThrow(() -> new IllegalArgumentException("Loan not found"));
        loan.setStatus("DENIED");
        loanRepo.save(loan);
    }

    // ---------- EMI calculation ----------

    private BigDecimal calculateEmi(BigDecimal principal, BigDecimal annualRate, int months) {
        BigDecimal monthlyRate = annualRate
                .divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal onePlusRPowerN = monthlyRate
                .add(BigDecimal.ONE)
                .pow(months);

        BigDecimal numerator = principal
                .multiply(monthlyRate)
                .multiply(onePlusRPowerN);

        BigDecimal denominator = onePlusRPowerN.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_UP);
    }
}
