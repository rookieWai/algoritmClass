package leetcode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 1.两数之和
 * 给定一个整数数组 num和一个整数目标值 target，请你在该数组中找出 和为目标值 的那两个整数，并返回它们的数组下标。
 *
 * 你可以假设每种输入只会对应一个答案。但是，数组中同一个元素在答案里不能重复出现。
 *
 * 你可以按任意顺序返回答案。
 *

 */

public class TwoSum {
    //暴力解法
    public int[] twoSum(int[] nums, int target) {

        for (int i=0;i<nums.length;i++){
            for(int j=i+1;j< nums.length;j++){
                if(nums[i]+nums[j]==target){
                    return new int[]{i,j};
                }
            }
        }
        return new int[0];
    }

    //哈希表

    public int[] twoSum2(int[] nums, int target) {

        HashMap hashTable=new HashMap<Integer,Integer>();

        for (int i=0;i<nums.length;i++){
            if(hashTable.containsKey(target-nums[i])){
                return new int []{(int) hashTable.get(target-nums[i]),i};
            }
            hashTable.put(nums[i],i);
        }
        return new int[0];
    }
}
