package leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * 349.两个数组的交集
 * 给定两个数组，编写一个函数来计算它们的交集。
 */

public class Intersection {
    public int[] intersection(int[] nums1, int[] nums2) {
        Set hashTable1=new HashSet();
        Set hashTable2=new HashSet();
        int n=0;

        //把nums1映射到hashTable1中
        for(int i : nums1){
            hashTable1.add(i);
        }

        //遍历nums2，获取交集
        for(int i : nums2){
            //判断hashTable1中是否存在该元素
            if(hashTable1.contains(i)){
                //存在放入交集
                hashTable2.add(i);
            }
        }

        //把交集元素放入数组中
        int []result=new int[hashTable2.size()];
        for(Object i : hashTable2){
            result[n++]= (int) i;
        }
        return result;
    }
}
