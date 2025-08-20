package com.kadioglumf.coensio.garanti;

import java.util.Arrays;

public class Question1 {

    /*
    * Write a function that returns the largest (maximum) element in a list containing only positive integers.
    *
    * Example:
    *
    * Input: [1, 3, 5, 12, 4 ,21 ,3, 2]
    * Output: 21
    *
    *Input: [1]
    *Output: 1
    *
    * Constraints:
    * 1 <= length of list <= 100
    * */

    public static void main(String[] args) {
        int[] arr1 = {1, 3, 5, 12, 4, 21, 3, 2};
        System.out.println(max(arr1)); // 21
    }

    private static int max(int[] nums) {
        int max = nums[0];
        for (int num : nums) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }

    private static int max2(int[] nums) {
        return Arrays.stream(nums)
                .max()
                .getAsInt();
    }
}
