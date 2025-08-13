package com.kadioglumf;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

public class Main {
    public static void main(String[] args) {
        var list = DataGenerator.generateCustomers(100);
        var firstCustomer = list.getFirst();

        // Bir müşterinin tüm hesaplarındaki bakiye toplamını bulun.
        var sum = firstCustomer.getAccounts().stream().mapToDouble(Account::getBalance).sum();

        // Bakiyesi pozitif olan hesapları listeleyin.
        var positiveAccount = firstCustomer.getAccounts().stream().filter(b -> b.getBalance() > 0).toList();

        // Tüm müşterilerin yaş ortalamasını hesaplayın.
        var ageAvg = list.stream().mapToInt(Customer::getAge).average().orElse(0);

        // Son 2 yıl içinde açılmış vadeli hesapları filtreleyin
        var accounts = firstCustomer.getAccounts().stream()
                .filter(a -> "Vadeli".equals(a.getType()))
                .filter(a -> LocalDate.now().minusYears(2).isBefore(a.getOpenDate())).toList();

        // Hesapları türüne göre gruplayın
        var groupedAccounts = firstCustomer.getAccounts().stream()
                .collect(Collectors.groupingBy(Account::getType));

        // Limiti 15.000 TL üzeri kredi kartlarını bulun
        var creditCards = list.stream()
                .flatMap(c -> c.getAccounts().stream()
                .map(Account::getCreditCard)
                .filter(Objects::nonNull)
                .filter(cc -> cc.getLimit() > 15000))
                .toList();

        // Tüm müşterilerin kredi kartı borçlarının toplamını hesaplayın
        var totalDebt = list.stream()
                .flatMap(c -> c.getAccounts().stream().map(Account::getCreditCard))
                .filter(Objects::nonNull)
                .mapToDouble(CreditCard::getDebt)
                .sum();

        // Bakiyesi 10.000 TL’den yüksek olan hesap sayısını bulun
        long countHighBalance = firstCustomer.getAccounts().stream()
                .filter(a -> a.getBalance() > 10000)
                .count();

        // Bakiyesine göre en yüksek 10 hesabı sıralayın
        var top10Accounts = firstCustomer.getAccounts().stream()
                .sorted(Comparator.comparingDouble(Account::getBalance).reversed())
                .limit(10)
                .toList();

        // Müşteri ID’sine göre hesap listesini Map olarak oluşturun
        var accountsByCustomer = list.stream().collect(Collectors.toMap(Customer::getId, Customer::getAccounts));

        // Her müşteri için minimum ve maksimum hesap bakiyelerini bulun
        var minMaxByCustomer = list.stream().collect(Collectors.toMap(c -> c, c -> {
            DoubleSummaryStatistics stats = c.getAccounts().stream()
                    .collect(Collectors.summarizingDouble(Account::getBalance));
            return new Double[]{stats.getMin(), stats.getMax()};
        }));

        // Müşterilere göre kredi kartı borçlarının ortalamasını hesaplayın
        var avgDebtPerCustomer = list.stream()
                .collect(Collectors.toMap(c -> c, c -> {
                    DoubleSummaryStatistics stats = c.getAccounts().stream()
                            .filter(a -> a.getCreditCard() != null)
                            .collect(Collectors.summarizingDouble(a -> a.getCreditCard().getDebt()));
                    return stats.getAverage();
                }));

        // Müşterilere göre kredi kartı borçlarının ortalamasını hesaplayın
        Map<Customer, Double> avgDebtPerCustomer2 = list.stream()
                .collect(Collectors.toMap(
                        c -> c,
                        c -> c.getAccounts().stream()
                                .map(Account::getCreditCard)
                                .filter(Objects::nonNull)
                                .mapToDouble(CreditCard::getDebt)
                                .average()
                                .orElse(0.0)
                ));

        // Müşterileri şehir ve yaş bazında gruplayın
        var grouped = list.stream()
                        .collect(Collectors.groupingBy(
                                Customer::getCity,
                                Collectors.groupingBy(Customer::getAge)
                        ));

        // Toplam kredi kartı limiti en yüksek 5 müşteriyi bulun
        var customer5 = list.stream()
                        .sorted(Comparator.comparingDouble(
                                        (Customer c) -> c.getAccounts()
                                        .stream()
                                        .map(Account::getCreditCard)
                                        .filter(Objects::nonNull)
                                        .mapToDouble(CreditCard::getLimit)
                                        .sum())
                                .reversed()
                        )
                .limit(5).toList();

        // Toplam kredi kartı limiti en yüksek 5 müşteriyi bulun
        var top5 = list.stream()
                .map(c -> Map.entry(
                        c,
                        c.getAccounts().stream()
                                .map(Account::getCreditCard)
                                .filter(Objects::nonNull)
                                .mapToDouble(CreditCard::getLimit)
                                .sum()
                ))
                .sorted(Map.Entry.<Customer, Double>comparingByValue().reversed())
                .limit(5)
                .map(Map.Entry::getKey)
                .toList();

        // Her müşteri için en çok kullanılan hesap türünü tespit edin
        var mostCommonType = list.stream().collect(Collectors.toMap(
                c -> c,
                c -> c.getAccounts()
                        .stream()
                        .collect(Collectors.groupingBy(Account::getType, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElseThrow()

        ));

        // Müşterileri isimlerinin ilk harfine göre gruplayıp, her grup için ortalama yaş hesaplayın
        var avgAgeByInitial = list.stream()
                .collect(Collectors.groupingBy(c -> c.getName().charAt(0), Collectors.averagingInt(Customer::getAge)));

        System.out.println();
    }


}