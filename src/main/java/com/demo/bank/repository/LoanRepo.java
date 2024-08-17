package com.demo.bank.repository;

import com.demo.bank.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepo extends JpaRepository<Loan, Long> {

    @Query(value = "select * from loan l where l.status=?1", nativeQuery = true)
    List<Loan> findByStatus(String status);
}
