package com.kadioglumf.coensio.garanti;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Question2 {

/*
* Write a method to return duplicate characters (occurrence >= 2)
* from an input string in the same order they are in the input.
*
*Each duplicate character must exist only once in the output string.
*
* Example:
* Input: "happy"
* Output: "p"
*
* Input: "improper"
* Output: "pr"
*
* Constraints:
* length of the input string <= 30
* Input string is all lowercase and no whitespace
* Output string is all lowercase and no whitespace
*/

    public static void main(String[] args) {
        System.out.println( duplicateChars("improper"));;

    }

    private static String duplicateChars(String input) {
        Map<Character, Integer> map = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Objects.nonNull(map.get(c)) && map.get(c) == 1) {
                sb.append(c);
            }
            else {
                map.put(c, 1);
            }
        }
        return sb.toString();
    }
}
