package com.demo.bank.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Transfer {
    private long fromAccount;
    private long toAccount;
    private Double amount;
}
