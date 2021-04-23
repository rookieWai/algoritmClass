package leetcode;

import java.util.HashMap;
import java.util.Map;

/**
 * 给定四个包含整数的数组列表A , B , C , D ,计算有多少个元组 (i, j, k, l)，使得A[i] + B[j] + C[k] + D[l] = 0。
 *
 * 为了使问题简单化，所有的 A, B, C, D 具有相同的长度N，且 0 ≤ N ≤ 500 。所有整数的范围在 -228 到 228 - 1 之间，最终结果不会超过231 - 1 。
 *
 */
public class FourSumCount {
    public int fourSumCount(int[] nums1, int[] nums2, int[] nums3, int[] nums4) {
        //创建哈希表
        Map<Integer, Integer> hashTable=new HashMap();
        //记录满足的条件的结果数目
        int count=0;

        //遍历nums1
        for(int i:nums1){
            //遍历nums2
            for(int j:nums2){
                //将nums1与nums2的元素所有组合的和放入哈希表中，并放入和出现的次数，使用hashTable.getOrDefault获取已保存的大小然后加1
                hashTable.put(i+j,hashTable.getOrDefault(i+j,0)+1);
            }
        }


        //遍历nums3
        for(int i:nums3){
            //遍历nums4
            for(int j:nums4){
                //判断是否有满足条件的组合
                if(hashTable.containsKey(-i-j)){
                    //存储结果次数
                    count+=hashTable.get(-i-j);
                }
            }
        }

        return count;
    }
}
