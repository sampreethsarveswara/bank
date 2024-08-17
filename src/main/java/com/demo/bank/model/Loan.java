package com.demo.bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Loan implements Serializable {

    @Id
    @GeneratedValue
    private long id;
    private String applicantName;
    private double amountRequested;
    private String status;
    private long accountId;
    private double compoundInterest;

    public Loan(String applicantName, double amountRequested, String status, long accountId) {
        this.applicantName = applicantName;
        this.amountRequested = amountRequested;
        this.status = status;
        this.accountId = accountId;
        this.compoundInterest = compoundInterest();
    }

    public Double compoundInterest() {
        double principal = this.amountRequested, rate = 10.25, time = 3;

        double CI = principal *
                (Math.pow((1 + rate / 100), time));
        System.out.println("CI:" + CI);
        return CI;
    }
}
