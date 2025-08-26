package com.kadioglumf.obss;

import java.util.Arrays;

public class Anagram {

    public static void main(String[] args) {
        System.out.println(isAnagram("ABC CD1", "BDA DV!")); //false
        System.out.println(isAnagram("Bursa dağı!", "Su bardağı.")); //true
    }

    public static boolean isAnagram(String s1, String s2) {
        String clean1 = s1.replaceAll("[^a-zA-Z]", "").toLowerCase();
        String clean2 = s2.replaceAll("[^a-zA-Z]", "").toLowerCase();

        if (clean1.length() != clean2.length()) return false;

        char[] arr1 = clean1.toCharArray();
        char[] arr2 = clean2.toCharArray();

        Arrays.sort(arr1);
        Arrays.sort(arr2);

        return Arrays.equals(arr1, arr2);
    }
}
