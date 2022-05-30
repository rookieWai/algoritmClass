package leetcode;

import java.awt.font.NumericShaper;

/**
 * 剑指 Offer 21. 调整数组顺序使奇数位于偶数前面
 *
 * 输入一个整数数组，实现一个函数来调整该数组中数字的顺序，使得所有奇数位于数组的前半部分，所有偶数位于数组的后半部分。
 */

public class Exchange {
    public int[] exchange(int[] nums) {
        int left= 0,right=nums.length-1;

        while(left<right){
            if(nums[left]%2!=0){
                left++;
                continue;
            }
            if(nums[right]%2==0){
                right--;
                continue;
            }

            int temp=nums[left];
            nums[left]=nums[right];
            nums[right]=temp;

        }
        return nums;
    }
}
