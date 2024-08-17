package com.demo.bank.service;

import com.demo.bank.model.Account;
import com.demo.bank.model.Transaction;
import com.demo.bank.model.TransactionType;
import com.demo.bank.model.Transfer;
import com.demo.bank.repository.AccountRepo;
import com.demo.bank.repository.TransactionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private AccountService accountService;

    public Transaction addTransaction(Transaction transaction) {
        transaction.setTime(LocalDateTime.now());
        Transaction savedTransaction = transactionRepo.save(transaction);
        Account account = accountService.retrieveAccountDetails(transaction.getAccountId());
        updateAccountBalance(account, savedTransaction);
        return savedTransaction;
    }

    private void updateAccountBalance(Account account, Transaction transaction) {
        Double currentBalance = account.getBalance();
        if (transaction.getTransactionType().equalsIgnoreCase(TransactionType.DEPOSIT.toString())) {
            currentBalance = currentBalance + transaction.getAmount();
            account.setBalance(currentBalance);
        }
        if (transaction.getTransactionType().equalsIgnoreCase(TransactionType.WITHDRAW.toString())) {
            currentBalance = currentBalance - transaction.getAmount();
            account.setBalance(currentBalance);
        }
        accountRepo.save(account);
    }

    public Transaction retrieveTransactionDetails(long transactionId) {
        Optional<Transaction> optionalTransaction = transactionRepo.findById(transactionId);
        if (optionalTransaction.isEmpty()) {
            return null;
        }
        return optionalTransaction.get();
    }

    public List<Transaction> retrieveAllTransactionsByAccountId(long accountId, Map<String, String> headers) {
        if (Objects.nonNull(headers) && !headers.isEmpty() && headers.containsKey("type") && headers.containsKey("minimum")) {
            return transactionRepo.findByAccountIdWithTypeAndMinAmount(accountId, headers.get("type"), Double.valueOf(headers.get("minimum")));
        }
        if (Objects.nonNull(headers) && !headers.isEmpty() && headers.containsKey("type")) {
            return transactionRepo.findByAccountIdWithType(accountId, headers.get("type"));
        }
        if (Objects.nonNull(headers) && !headers.isEmpty() && headers.containsKey("minimum")) {
            return transactionRepo.findByAccountIdWithMinAmount(accountId, Double.valueOf(headers.get("minimum")));
        }
        List<Transaction> transactions = transactionRepo.findByAccountId(accountId);
        return transactions;
    }

    public Object transferMoney(Transfer transfer) {
        Account fromAccount = accountService.retrieveAccountDetails(transfer.getFromAccount());
        if (fromAccount.getBalance() < transfer.getAmount()) {
            return null;
        }
        Account toAccount = accountService.retrieveAccountDetails(transfer.getToAccount());
        Transaction withDrawTransaction = new Transaction(fromAccount.getId(), transfer.getAmount(),
                TransactionType.WITHDRAW.toString(), LocalDateTime.now());
        Transaction depositTransaction = new Transaction(toAccount.getId(), transfer.getAmount(),
                TransactionType.DEPOSIT.toString(), LocalDateTime.now());
        fromAccount.setBalance(fromAccount.getBalance() - transfer.getAmount());
        toAccount.setBalance(toAccount.getBalance() + transfer.getAmount());
        transactionRepo.save(withDrawTransaction);
        transactionRepo.save(depositTransaction);
        accountRepo.save(fromAccount);
        accountRepo.save(toAccount);
        return true;
    }
}
