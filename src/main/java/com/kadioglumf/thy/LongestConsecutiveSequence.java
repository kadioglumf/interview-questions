package com.kadioglumf.thy;

import java.util.Arrays;

public class LongestConsecutiveSequence {

    public static void main(String[] args) {
        int[] nums1 = {1, 100, 99, 3, 4, 2};
        int[] nums2 = {5, 2, 7, 3, 99, 100};
        int[] nums3 = {1, 2, 2, 3, 4};
        int[] nums4 = {1, 2, 3, 10, 11, 12};

        System.out.println(longestConsecutive(nums1)); // 4
        System.out.println(longestConsecutive(nums2)); // 2
        System.out.println(longestConsecutive(nums3)); // 4
        System.out.println(longestConsecutive(nums4)); // 3
    }

    private static int longestConsecutive(int[] nums) {
        Arrays.sort(nums);
        int maxCounter = 1;
        int counter = 1;

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i - 1]) {
                continue;
            }
            if (nums[i] == nums[i - 1] + 1) {
                counter++;
            } else {
                counter = 1;
            }
            maxCounter = Math.max(maxCounter, counter);
        }
        return maxCounter;
    }
}
