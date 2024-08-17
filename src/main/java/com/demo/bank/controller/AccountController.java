package com.demo.bank.controller;

import com.demo.bank.model.Account;
import com.demo.bank.model.Loan;
import com.demo.bank.model.Transaction;
import com.demo.bank.model.Transfer;
import com.demo.bank.service.AccountService;
import com.demo.bank.service.LoanService;
import com.demo.bank.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private LoanService loanService;

    @PostMapping("accounts")
    public ResponseEntity<?> createAccount(@RequestBody Account account) {
        if (Objects.isNull(account)) {
            return ResponseEntity.badRequest().build();
        }
        Account savedAccount = accountService.createAccount(account);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{accountId}")
                .buildAndExpand(savedAccount.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("accounts/{accountId}")
    public Account getAccountDetails(@PathVariable long accountId) {
        Account account = accountService.retrieveAccountDetails(accountId);
        if (Objects.isNull(account)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return account;
    }

    @PutMapping("accounts")
    public ResponseEntity<?> updateAccount(@RequestBody Account toBeUpdatedAccount) {
        if (Objects.isNull(toBeUpdatedAccount)) {
            return ResponseEntity.badRequest().build();
        }
        Account updatedAccount = accountService.updateAccountDetails(toBeUpdatedAccount);
        if (Objects.isNull(updatedAccount)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedAccount);
    }

    @DeleteMapping("accounts/{accountId}")
    public ResponseEntity<?> deleteAccount(@PathVariable long accountId) {
        Object deleted = accountService.deleteAccount(accountId);
        if (Objects.isNull(deleted)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("transactions")
    public ResponseEntity<?> createTransaction(@RequestBody Transaction transaction) {
        if (Objects.isNull(transaction)) {
            return ResponseEntity.badRequest().build();
        }
        Transaction savedTransaction = transactionService.addTransaction(transaction);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{transactionId}")
                .buildAndExpand(savedTransaction.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("transactions/{transactionId}")
    public Transaction getTransactionDetails(@PathVariable long transactionId) {
        Transaction transaction = transactionService.retrieveTransactionDetails(transactionId);
        if (Objects.isNull(transaction)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return transaction;
    }

    @GetMapping("accounts/transactions/{accountId}")
    public List<Transaction> getAllTransactions(@RequestHeader Map<String, String> headers,
                                                @PathVariable long accountId) {
        List<Transaction> transactions = transactionService.retrieveAllTransactionsByAccountId(accountId, headers);
        return transactions;
    }

    @PostMapping("accounts/transfer")
    public ResponseEntity<?> transferFunds(@RequestBody Transfer transfer) {
        if (Objects.isNull(transfer)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Object object = transactionService.transferMoney(transfer);
        if (Objects.isNull(object)) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(object);
    }

    @PostMapping("accounts/loans")
    public ResponseEntity<?> createLoanApplication(@RequestBody Loan loan) {
        if (Objects.isNull(loan)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Loan createdLoanApplication = loanService.createLoanApplication(loan);
        if (Objects.isNull(createdLoanApplication)) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(createdLoanApplication);
    }

    @PutMapping("accounts/loans")
    public ResponseEntity<?> updateLoanApplication(@RequestBody Loan loan) {
        if (Objects.isNull(loan)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        Loan updatedLoanApplication = loanService.updateLoanApplication(loan);
        if (Objects.isNull(updatedLoanApplication)) {
            throw new ResponseStatusException(HttpStatus.EXPECTATION_FAILED);
        }
        return ResponseEntity.ok(updatedLoanApplication);
    }

    @GetMapping("accounts/loans/pending")
    public ResponseEntity<?> transferFunds() {
        List<Loan> pendingLoanApplications = loanService.getPendingLoanApplication();
        return ResponseEntity.ok(pendingLoanApplications);
    }
}
