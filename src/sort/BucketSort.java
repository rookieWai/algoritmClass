package sort;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class BucketSort {
    public static void sort(int arr[]){
        int max=Integer.MIN_VALUE;
        int min=Integer.MAX_VALUE;

        //求最大最小值
        for(int i=0;i<arr.length;i++ ){
            max=Math.max(max,arr[i]);
            min=Math.min(min,arr[i]);
        }

        //桶的数量
        int bucketNum=(max-min)/arr.length+1;
        ArrayList<ArrayList<Integer>> bucketArr=new ArrayList<>(bucketNum);
        for(int i=0;i<bucketNum;i++){
            bucketArr.add(new ArrayList<Integer>());
        }

        //将元素放入桶中
        for(int i=0;i<arr.length;i++){
            int num=(arr[i]-min)/(arr.length);
            bucketArr.get(num).add(arr[i]);
        }

        //对每个桶进行排序
        for(int i=0;i<bucketNum;i++){
            Collections.sort(bucketArr.get(i));
            for(int j=0;j<bucketArr.get(i).size();j++){
                arr[j]=bucketArr.get(i).get(j);
            }
        }
    }
}
