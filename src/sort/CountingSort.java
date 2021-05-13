package sort;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * 计数排序
 */
public class CountingSort {
    public static void sort(int arr[]){
        //如果数组长度为0，直接结束
        if(arr.length==0){
            return;
        }

        int min=arr[0];
        int max=arr[0];

        //求数组最大数和最小数,用于开辟计数数组
        for(int i=0;i<arr.length;i++){
            min=Math.min(min,arr[i]);
            max=Math.max(max,arr[i]);
        }

        //开辟计数数组
        int[] C=new int[max-min+1];
        //初始化为0
        Arrays.fill(C,0);

        //遍历待排序数组，用C来计数
        for (int i = 0; i < arr.length; i++) {
            C[arr[i]-min]++;
        }

        int j=0;
        //将C中记录的数按顺序放入arr中
        for (int i = 0; i < C.length; i++) {
            int temp=C[i];
            while(temp>0){
                arr[j++]=i+min;
                temp--;
            }
        }

    }
}
