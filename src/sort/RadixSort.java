package sort;

import java.util.ArrayList;
import java.util.Collections;

public class RadixSort {
    public static void sort(int arr[]){

        //如果数组长度小于等于1或者为空，直接结束
        if(arr==null&&arr.length<2){
            return;
        }

        //求数组的最大值
        int max=Integer.MIN_VALUE;
        for (int i = 0; i < arr.length; i++) {
            max=Math.max(max,arr[i]);
        }

        //根据最大值求最大位数
        int maxDigit=0;
        while(max!=0){
            max/=10;
            maxDigit++;
        }

        //创建桶
        ArrayList<ArrayList<Integer>> bucket=new ArrayList<>();
        //初始化桶
        for (int i = 0; i < 10; i++) {
            bucket.add(new ArrayList<Integer>());
        }

        //取模和做出元素，得到元素每一位的的数
        int mod=10;
        int div=1;

        for(int i=0;i<maxDigit;i++){
            //将待排序元素放入对应的桶
            for (int j = 0; j < arr.length; j++) {
                int num=arr[i]%mod/div;
                bucket.get(num).add(arr[j]);
            }

            //将每个桶中的数排好序后，依次放回原数组
            int index=0;
            for (int j = 0; j < bucket.size(); j++) {
                Collections.sort(bucket.get(j));
                for(int k=0;k<bucket.get(j).size();k++){
                    arr[index++]=bucket.get(j).get(k);
                }
                bucket.get(j).clear();
            }
        }
    }
}
