package com.demo.bank.repository;

import com.demo.bank.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, Long> {

    @Query(value = "select * from transaction t where t.account_id=?1", nativeQuery = true)
    List<Transaction> findByAccountId(long id);

    @Query(value = "select * from transaction t where t.account_id=?1 and t.transaction_type=?2", nativeQuery = true)
    List<Transaction> findByAccountIdWithType(long id, String transactionType);

    @Query(value = "select * from transaction t where t.account_id=?1 and t.amount>?2", nativeQuery = true)
    List<Transaction> findByAccountIdWithMinAmount(long id, double amount);

    @Query(value = "select * from transaction t where t.account_id=?1 and t.transaction_type=?2 and t.amount>?3", nativeQuery = true)
    List<Transaction> findByAccountIdWithTypeAndMinAmount(long id, String transactionType, double amount);
}
