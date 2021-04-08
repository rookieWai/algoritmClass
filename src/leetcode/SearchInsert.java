package leetcode;


/**
 * 问题
 * 35.搜索插入位置
 * 给定一个排序数组和一个目标值，在数组中找到目标值，并返回其索引。如果目标值不存在于数组中，返回它将会被按顺序插入的位置。
 *
 * 问题分析
 * 暴力法:
 * 使用for循环，
 *
 * 二分查找法
 */
public class SearchInsert {

    //暴力法
    public static int searchInsert1(int[] nums, int target) {
        for(int i=0;i<nums.length;i++){
            if(nums[i]>=target){
                return i;   //记录i
            }
        }

        return nums.length;  //
    }
    //二分法
   public static int searchInsert2(int[] nums, int target) {
        int n=nums.length;
        int left=0,right=n-1;

        while(left<=right){

            int middle=left+(right-left)/2;
            if(target>nums[middle]){
                left=middle+1;
            }
            else if(target<nums[middle]){
                right=middle-1;
            }
            else{
                return middle;
            }

        }
        return right+1;
    }

}

