package leetcode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *15.三数之和
 *
 * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有和为 0 且不重复的三元组。
 * 注意：答案中不可以包含重复的三元组。
 */
public class ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        //存储三元组的数组
        List<List<Integer>> result = new ArrayList<>();
        //对数组进行排序
        Arrays.sort(nums);




        for(int i=0;i<nums.length;i++) {
            //因为是排好序，从小到大，如果a大于零，表示不存在满足条件的三元组
            if (nums[i] > 0) {
                return result;
            }

            //对a做去重操作
            if (i > 0 && nums[i - 1] == nums[i]) {
                continue;
            }

            int left = i + 1;
            int right = nums.length - 1;

            while (left < right) {
                if (nums[i] + nums[left] + nums[right] > 0) {
                    //和大于零，右边指针向左移动，即c值大小减小
                    right--;
                } else if ( nums[i] + nums[left] + nums[right] < 0) {
                    //和小于零，左边指针向右移动，即b值大小增大
                    left++;
                } else {
                    //和等于零，保存结果
                    List<Integer> ok = new ArrayList<>();
                    ok.add(nums[i]);
                    ok.add(nums[left]);
                    ok.add(nums[right]);
                    result.add(ok);

                    //对b,c做去重处理
                    while (left < right && nums[right] == nums[right-1]) right--;
                    while (left < right && nums[left] == nums[left+1]) left++;

                    //找到一个结果后，两指针同时移动到下一个位置
                    right--;
                    left++;
                }
            }
        }
        return result;
    }

}
