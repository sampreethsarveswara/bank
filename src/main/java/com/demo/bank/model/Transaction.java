package com.demo.bank.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue
    private Long id;
    private long accountId;
    private Double amount;
    private String transactionType;
    private LocalDateTime time;

    public Transaction(long accountId, Double amount, String transactionType, LocalDateTime time) {
        this.accountId = accountId;
        this.amount = amount;
        this.transactionType = transactionType;
        this.time = time;
    }
}
