package com.kadioglumf;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class DataGenerator {

    private static final String[] NAMES = {"Ahmet", "Mehmet", "Ayşe", "Fatma", "Ali", "Veli", "Selin", "Deniz", "Burak", "Ece"};
    private static final String[] CITIES = {"Istanbul", "Ankara", "Izmir", "Bursa", "Antalya"};
    private static final String[] ACCOUNT_TYPES = {"Vadesiz", "Vadeli", "Kredi"};

    public static List<Customer> generateCustomers(int count) {
        List<Customer> customers = new ArrayList<>(count);

        for (int i = 1; i <= count; i++) {
            String id = "C" + String.format("%04d", i);
            String name = NAMES[new Random().nextInt(NAMES.length)] + " " + (char)(65 + new Random().nextInt(26)) + ".";
            String city = CITIES[new Random().nextInt(CITIES.length)];
            int age = 18 + new Random().nextInt(50); // 18-67 yaş arası

            int accountCount = 1 + new Random().nextInt(5); // 1-5 hesap
            List<Account> accounts = new ArrayList<>();

            for (int j = 1; j <= accountCount; j++) {
                String accountNumber = "ACC" + id + j;
                String type = ACCOUNT_TYPES[new Random().nextInt(ACCOUNT_TYPES.length)];
                double balance = ThreadLocalRandom.current().nextDouble(-5000, 20000);

                LocalDate openDate = LocalDate.now().minusDays(new Random().nextInt(365 * 5)); // son 5 yıl içinde

                CreditCard creditCard = null;
                if ("Kredi".equals(type) && new Random().nextBoolean()) { // %50 ihtimalle kredi kartı
                    String cardNumber = "CARD" + id + j;
                    LocalDate expiryDate = LocalDate.now().plusDays(new Random().nextInt(365 * 3));
                    double limit = ThreadLocalRandom.current().nextDouble(14000, 20000);
                    double debt = ThreadLocalRandom.current().nextDouble(0, limit);
                    creditCard = new CreditCard(cardNumber, expiryDate, limit, debt);
                }

                accounts.add(new Account(accountNumber, type, balance, openDate, creditCard));
            }

            customers.add(new Customer(id, name, city, age, accounts));
        }

        return customers;
    }
}

