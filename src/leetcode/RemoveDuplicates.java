package leetcode;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;

public class RemoveDuplicates {
    public int removeDuplicates(int[] nums) {
        int num=0;
        int n=nums.length;

        for(int i=0;i<n;i++){
            num++;
            if(nums[i]!=nums[i+1]){
                while(num-->2){
                    remove(nums,n,i-num);
                    i--;
                    n--;
                }
                num=0;
            }
        }
        return n;
    }

    public void remove(int[] nums,int length,int index){
        for(int i=index;i<length-1;i++){
            int temp=nums[i];
            nums[i]=nums[i+1];
            nums[i+1]=nums[i];
        }
    }
}
