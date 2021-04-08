package leetcode;


/**
 * 给你一个数组 num和一个值 val，你需要原地移除所有数值等于val的元素，并返回移除后数组的新长度。
 *
 * 不要使用额外的数组空间，你必须仅使用 O(1) 额外空间并 原地 修改输入数组。
 *
 * 元素的顺序可以改变。你不需要考虑数组中超出新长度后面的元素。
 *
 */

public class RemoveElement {

    //暴力解法
    public int removeElement(int[] nums, int val) {
        int length=nums.length;

        for(int i=0;i<length;i++){
            if(nums[i]==val){   //发现相同的元素，把这个元素后面的元素整体向前挪一位
                for(int j=i+1;j<length;j++){
                    nums[j-1]=nums[j];
                }
                i--;  //向前挪了一位，所以遍历指针也应该向前挪一位
                length--;  //数组长度减1
            }
        }

        return length;
    }


    //双指针

    public int removeElement2(int[] nums, int val) {
        int slowIndex=0;

        for(int fastIndex=0;fastIndex<nums.length;fastIndex++){
            if(val!=nums[fastIndex]){
                nums[slowIndex++]=nums[fastIndex];
            }
        }
        return slowIndex;
    }
}
