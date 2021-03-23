package sort.mergeSort;

import static sort.mergeSort.MergeTopDown.aux;

//原地归并的抽象方法,升序
public class Merge{
    public static void merge(int[] a,int lo,int mid,int hi){
        //将a[lo..mid]和[mind+1..hi]归并
        int i=lo,j=mid+1;

        //将a复制到aux辅助数组
        for(int k=lo;k<=hi;k++)
            aux[k]=a[k];

        //归并会a数组中
        for(int k=lo;k<=hi;k++){
            if(i>mid) a[k]=aux[j++];  //左边有序数组用尽，取右边的
            else if(j>hi) a[k]=aux[i++];  //右边有序数组用尽，取左边的
            else if(aux[i]<aux[j]) a[k]=aux[i++]; //左边数组小于右边数组，取左边
            else if(aux[i]>aux[j])a[k]=aux[j++];  //右边数组小于左边数组，取右边
            else {
                a[k++]=aux[i++];
                a[k]=aux[j++];
            }  //相等
        }

    }
}
