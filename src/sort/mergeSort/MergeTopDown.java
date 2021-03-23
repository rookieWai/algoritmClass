package sort.mergeSort;

//自顶向下的归并排序
public class MergeTopDown {

    public static int[] aux;


    public static void sort(int[] a){
        aux=new int [a.length];
        sort(a,0,a.length-1);
    }

    private static void sort(int[] a,int lo,int hi){
        if(hi<=lo) return;
        int mid=lo+(hi-lo)/2;
        sort(a,lo,mid);      //将左半边排序
        sort(a,mid+1,hi);  //将右半边排序
        Merge.merge(a,lo,mid,hi); //归并结果
    }
}


