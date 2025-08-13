package com.kadioglumf;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private String accountNumber;
    private String type; // Vadesiz, Vadeli, Kredi
    private double balance;
    private LocalDate openDate;
    private CreditCard creditCard; // opsiyonel
}
