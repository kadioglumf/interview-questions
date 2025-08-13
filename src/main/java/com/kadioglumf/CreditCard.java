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
public class CreditCard {
    private String cardNumber;
    private LocalDate expiryDate;
    private double debt;
    private double limit;

}
