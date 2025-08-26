package com.kadioglumf.thy;

import java.util.*;

public class CharCounter {

    public static Map<Character, Integer> countAll(String input) {
        Map<Character, Integer> counts = new LinkedHashMap<>();

        if (input == null || input.isEmpty()) return counts;

        input = input.replaceAll("\\s+", "").toLowerCase();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            counts.put(c, counts.getOrDefault(c, 0) + 1);
        }

        return counts;
    }

    public static void main(String[] args) {
        String input = "bilaL ";
        Map<Character, Integer> result = countAll(input);

        result.forEach((ch, cnt) -> System.out.println(ch + " -> " + cnt));
    }
}


