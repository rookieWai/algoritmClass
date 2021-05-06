package leetcode;

/**
 *239. 滑动窗口最大值
 *
 * 给你一个整数数组 nums，有一个大小为k的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k个数字。滑动窗口每次只向右移动一位。
 *
 * 返回滑动窗口中的最大值。
 *

 */

import java.util.LinkedList;

public class MaxSlidingWindow {

    public int[] maxSlidingWindow(int[] nums, int k) {

        //如果数组为空，或者小于2直接返回
        if(nums == null || nums.length < 2) {
            return nums;
        }

        // 单调队列，从大到小保存当前窗口中的值，不一定是全部值，是可能成为滑动窗口最大值的元素
        LinkedList<Integer> queue = new LinkedList();

        // 保存结果数组
        int[] result = new int[nums.length-k+1];

        // 遍历nums数组，实现滑动窗口的操作
        for(int i = 0;i < nums.length;i++){

            // 保证队列里的元素从大到小 如果队尾元素小于当前待入队元素，则需要依次弹出，直至满足要求
            while(!queue.isEmpty() && queue.peekLast() < nums[i]){
                queue.pollLast();
            }

            //将元素加入队列
            queue.addLast(nums[i]);

            // 判断当前队列中队首元素是否为滑动后需要移除的元素，滑动窗口的范围为[i-k+1,i]
            if(i+1 > k && queue.peek() <= nums[i-k]){
                queue.poll();
            }

            //保存窗口中最大值
            if(i+1 >= k){
                result[i+1-k] = queue.peek();
            }
        }
        return result;
    }
}
