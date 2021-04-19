package leetcode;


/**
 * 209. 长度最小的子数组
 * 给定一个含有n正整数的数组和一个正整数 target 。
 *
 * 找出该数组中满足其和 ≥ target 的长度最小的 连续子数组[numsl, numsl+1, ..., numsr-1, numsr] ，并返回其长度。如果不存在符合条件的子数组，返回 0 。
 *
 *
 */

public class MinSubArrayLen {
    //暴力解法
    public int minSubArrayLen(int target, int[] nums) {
        int result=Integer.MAX_VALUE;    //记录满足条件的最小连续数组的长度
        int sum=0;       //记录数组和
        int nowLength=0; //记录当前数组长度

        for(int i=0;i<nums.length;i++){
            sum=0;
            for(int j=i;j<nums.length;j++){
                sum+=nums[j];
                //数组满足条件时，记录结果
                if(sum>=target){
                    nowLength=j-i+1;  //当前的数组长度
                    result=result<nowLength?result:nowLength; //判断当前数组长度是否比已记录的小，是则保存
                    break; //结束循环
                }
            }
        }
        return result==Integer.MAX_VALUE?0:result;
    }

    //双指针(滑动窗口)
    public int minSubArrayLen2(int target, int[] nums) {
        int result=Integer.MAX_VALUE;    //记录满足条件的最小连续数组的长度
        int sum=0;       //记录数组和
        int nowLength=0; //记录当前数组长度
        int i=0;  //起始位置

        for(int j=0;j<nums.length;j++){
            sum+=nums[j];
            while(sum>target){
                nowLength=j-i+1;
                result=result<nowLength?result:nowLength; //判断当前数组长度是否比已记录的小，是则保存
                sum-=nums[i++];  //起始位置向前移动
            }
        }

        return result==Integer.MAX_VALUE?0:result;
    }

}
