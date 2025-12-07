package com.myfinbank.customer.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.myfinbank.customer.dto.AccountCreateRequest;
import com.myfinbank.customer.dto.AccountSummaryView;
import com.myfinbank.customer.dto.TransactionView;
import com.myfinbank.customer.model.BankAccount;
import com.myfinbank.customer.model.CustomerProfile;
import com.myfinbank.customer.entity.Transaction;
import com.myfinbank.customer.entity.TransactionType;
import com.myfinbank.customer.repository.BankAccountRepository;
import com.myfinbank.customer.repository.CustomerProfileRepository;
import com.myfinbank.customer.repository.TransactionRepository;

@Service
public class AccountManagementService {

    private final CustomerProfileRepository customerRepo;
    private final BankAccountRepository accountRepo;
    private final TransactionRepository txRepo;
    private final RestTemplate restTemplate;
    private final Random random = new Random();

    public AccountManagementService(CustomerProfileRepository customerRepo,
                                    BankAccountRepository accountRepo,
                                    TransactionRepository txRepo,
                                    RestTemplate restTemplate) {
        this.customerRepo = customerRepo;
        this.accountRepo = accountRepo;
        this.txRepo = txRepo;
        this.restTemplate = restTemplate;
    }

    @Transactional
    public AccountSummaryView openAccount(AccountCreateRequest request) {
        CustomerProfile owner = customerRepo.findById(request.getCustomerId())
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        String accountNumber = generateAccountNumber();

        BankAccount account = new BankAccount();
        account.setOwner(owner);
        account.setAccountNumber(accountNumber);
        account.setAccountType(request.getAccountType());

        BigDecimal opening = request.getOpeningBalance() != null
                ? request.getOpeningBalance()
                : BigDecimal.ZERO;
        account.setBalance(opening);
        account.setStatus("ACTIVE");

        BankAccount saved = accountRepo.save(account);

        Transaction tx = null;
        if (opening.compareTo(BigDecimal.ZERO) > 0) {
            tx = new Transaction();
            tx.setAccountNumber(saved.getAccountNumber());
            tx.setType(TransactionType.OPENING);
            tx.setAmount(opening);
            tx.setTimestamp(LocalDateTime.now());
            tx = txRepo.save(tx);
        }

        return toView(saved, tx);
    }

    @Transactional(readOnly = true)
    public List<AccountSummaryView> getAllAccounts() {
        return accountRepo.findAll()
                .stream()
                .map(acc -> toView(acc, null))
                .collect(Collectors.toList());
    }

    public List<AccountSummaryView> getAccountsForCustomer(Long customerId) {
        CustomerProfile owner = customerRepo.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        return accountRepo.findByOwner(owner).stream()
                .filter(acc -> "ACTIVE".equalsIgnoreCase(acc.getStatus()))
                .map(acc -> toView(acc, null))
                .collect(Collectors.toList());
    }


    @Transactional
    public AccountSummaryView depositToAccount(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        BankAccount account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalStateException("Only ACTIVE accounts can receive deposits");
        }

        account.setBalance(account.getBalance().add(amount));
        BankAccount saved = accountRepo.save(account);

        Transaction tx = new Transaction();
        tx.setAccountNumber(saved.getAccountNumber());
        tx.setType(TransactionType.DEPOSIT);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx = txRepo.save(tx);

        return toView(saved, tx);
    }

    @Transactional
    public AccountSummaryView withdrawFromAccount(String accountNumber, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        BankAccount account = accountRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Account not found"));

        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            throw new IllegalStateException("Only ACTIVE accounts can perform withdrawals");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance for withdrawal");
        }

        account.setBalance(account.getBalance().subtract(amount));
        BankAccount saved = accountRepo.save(account);

        Transaction tx = new Transaction();
        tx.setAccountNumber(saved.getAccountNumber());
        tx.setType(TransactionType.WITHDRAW);
        tx.setAmount(amount);
        tx.setTimestamp(LocalDateTime.now());
        tx = txRepo.save(tx);

        // zero-balance email alert
        if (saved.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            sendZeroBalanceAlert(saved);
        }

        return toView(saved, tx);
    }

    @Transactional
    public AccountSummaryView transferBetweenAccounts(String fromAccountNumber,
                                                      String toAccountNumber,
                                                      BigDecimal amount) {

        if (fromAccountNumber == null || toAccountNumber == null
                || fromAccountNumber.isBlank() || toAccountNumber.isBlank()) {
            throw new IllegalArgumentException("From and To account numbers are required");
        }
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("From and To accounts must be different");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }

        BankAccount from = accountRepo.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("From account not found"));

        BankAccount to = accountRepo.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new IllegalArgumentException("To account not found"));

        if (!"ACTIVE".equalsIgnoreCase(from.getStatus())
                || !"ACTIVE".equalsIgnoreCase(to.getStatus())) {
            throw new IllegalStateException("Both accounts must be ACTIVE for transfer");
        }

        if (from.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient balance in source account");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));

        BankAccount savedFrom = accountRepo.save(from);
        BankAccount savedTo = accountRepo.save(to);

        Transaction debit = new Transaction();
        debit.setAccountNumber(fromAccountNumber);
        debit.setType(TransactionType.TRANSFER_OUT);
        debit.setAmount(amount);
        debit.setTimestamp(LocalDateTime.now());
        txRepo.save(debit);

        Transaction credit = new Transaction();
        credit.setAccountNumber(toAccountNumber);
        credit.setType(TransactionType.TRANSFER_IN);
        credit.setAmount(amount);
        credit.setTimestamp(LocalDateTime.now());
        credit = txRepo.save(credit);

        // zero-balance email alert for FROM account
        if (savedFrom.getBalance().compareTo(BigDecimal.ZERO) == 0) {
            sendZeroBalanceAlert(savedFrom);
        }

        return toView(savedTo, credit);
    }

    private void sendZeroBalanceAlert(BankAccount account) {
        try {
            ZeroBalanceAlertRequest req = new ZeroBalanceAlertRequest();
            req.setCustomerId(account.getOwner().getId());
            req.setCustomerName(account.getOwner().getFullName());
            req.setAccountNumber(account.getAccountNumber());
            req.setBalance(account.getBalance());

            restTemplate.postForEntity(
                    "http://localhost:8084/api/notifications/zero-balance",
                    req,
                    Void.class
            );
        } catch (Exception ex) {
            // log and continue; do not break withdraw
            System.err.println("Failed to send zero-balance email: " + ex.getMessage());
        }
    }


    private AccountSummaryView toView(BankAccount account, Transaction tx) {
        AccountSummaryView view = new AccountSummaryView();
        view.setId(account.getId());
        view.setAccountNumber(account.getAccountNumber());
        view.setAccountType(account.getAccountType());
        view.setBalance(account.getBalance());
        view.setStatus(account.getStatus());
        view.setCustomerId(account.getOwner().getId());
        view.setCustomerName(account.getOwner().getFullName());
        if (tx != null) {
            view.setTransactionId(tx.getId());
        }
        return view;
    }

    public List<TransactionView> getTransactionsForAccount(String accountNumber) {
        return txRepo.findByAccountNumber(accountNumber).stream()
                .map(tx -> {
                    TransactionView v = new TransactionView();
                    v.setTransactionId(tx.getId());
                    v.setType(tx.getType().name());
                    v.setAccountNumber(tx.getAccountNumber());
                    v.setAmount(tx.getAmount());
                    v.setTimestamp(tx.getTimestamp());
                    return v;
                })
                .toList();
    }
    public List<BankAccount> getActiveAccountsForCustomer(Long customerId) {
        CustomerProfile owner = customerRepo.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        List<BankAccount> all = accountRepo.findByOwner(owner);
        return all.stream()
                  .filter(acc -> "ACTIVE".equalsIgnoreCase(acc.getStatus()))
                  .toList();
    }
    


    private String generateAccountNumber() {
        return "MF" + (10000000 + random.nextInt(90000000));
    }
}
