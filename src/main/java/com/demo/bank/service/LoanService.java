package com.demo.bank.service;

import com.demo.bank.model.Loan;
import com.demo.bank.model.LoanStatus;
import com.demo.bank.repository.LoanRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepo loanRepo;


    public Loan createLoanApplication(Loan loan) {
        loan.setCompoundInterest(loan.compoundInterest());
        return loanRepo.save(loan);
    }

    public Loan updateLoanApplication(Loan loan) {
        Optional<Loan> existingLoan = loanRepo.findById(loan.getId());
        if (existingLoan.isEmpty()) {
            return null;
        }
        existingLoan.get().setStatus(loan.getStatus());
        Loan updatedLoanApplication = loanRepo.save(existingLoan.get());
        return updatedLoanApplication;
    }

    public List<Loan> getPendingLoanApplication() {
        return loanRepo.findByStatus(LoanStatus.Pending.toString());
    }
}
