package com.demo.bank.service;

import com.demo.bank.model.Account;
import com.demo.bank.repository.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepo accountRepo;

    public Account createAccount(Account account) {
        Account savedAccount = accountRepo.save(account);
        return savedAccount;
    }

    public Account retrieveAccountDetails(long accountId) {
        Optional<Account> optionalAccount = accountRepo.findById(accountId);
        if (optionalAccount.isEmpty()) {
            return null;
        }
        return optionalAccount.get();
    }

    public Account updateAccountDetails(Account toBeUpdatedAccount) {
        Account existingAccount = retrieveAccountDetails(toBeUpdatedAccount.getId());
        if (Objects.isNull(existingAccount)) {
            return null;
        }
        existingAccount.setAccountHolderName(toBeUpdatedAccount.getAccountHolderName());
        accountRepo.save(existingAccount);
        return existingAccount;
    }

    public Object deleteAccount(long accountId) {
        Account existingAccount = retrieveAccountDetails(accountId);
        if (Objects.isNull(existingAccount)) {
            return null;
        }
        accountRepo.delete(existingAccount);
        return true;
    }
}
