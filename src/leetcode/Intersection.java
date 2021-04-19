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

        for(int i : nums1){
            hashTable1.add(i);
        }
        for(int i : nums2){
            if(hashTable1.contains(i)){
                hashTable2.add(i);
            }
        }

        int []result=new int[hashTable2.size()];
        for(Object i : hashTable2){
            result[n++]= (int) i;
        }
        return result;
    }
}
