package com.demo.bank.data;

import com.demo.bank.model.*;
import com.demo.bank.repository.AccountRepo;
import com.demo.bank.repository.LoanRepo;
import com.demo.bank.repository.TransactionRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class InitialDataLoader implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private TransactionRepo transactionRepo;

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private LoanRepo loanRepo;

    @Override
    public void run(String... args) throws Exception {

        Account account1 = new Account("Ganga", 0.0);
        Account account2 = new Account("Abhi", 0.0);
        Account account3 = new Account("Chandu", 0.0);
        logger.info("Bank Account Details Data Loading Started");
        accountRepo.save(account1);
        accountRepo.save(account2);
        accountRepo.save(account3);
        logger.info("Bank Account Details Data Loading Completed");

        Transaction transaction1 = new Transaction(account1.getId(), 10.0, TransactionType.DEPOSIT.toString(), LocalDateTime.now());
        Transaction transaction2 = new Transaction(account2.getId(), 2000.0, TransactionType.DEPOSIT.toString(), LocalDateTime.now());
        Transaction transaction3 = new Transaction(account3.getId(), 200000.0, TransactionType.DEPOSIT.toString(), LocalDateTime.now());
        logger.info("Transaction Details Data Loading Started");
        transactionRepo.save(transaction1);
        updateAccountBalance(account1, transaction1);
        transactionRepo.save(transaction2);
        updateAccountBalance(account2, transaction2);
        transactionRepo.save(transaction3);
        updateAccountBalance(account3, transaction3);
        logger.info("Transaction Details Data Loading Completed");

        logger.info("Loan Account Details Data Loading Started");
        loanRepo.save(new Loan("Ganga", 50000.0, LoanStatus.Pending.toString(), account1.getId()));
        loanRepo.save(new Loan("Chandu", 200000.0, LoanStatus.Pending.toString(), account3.getId()));
        logger.info("Loan Account Details Data Loading Completed");
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
}
