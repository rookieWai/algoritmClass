package sort;

public class MergeSort {

    //归并
    public static void merge(int arr[],int lo,int mid,int hi){
        int i=lo;
        int j=mid+1;

        //辅助数组
        int[] aux=new int[arr.length];

        for(int k=0;k<arr.length;k++){
            aux[k]=arr[k];
        }

        for(int k=lo;k<=hi;k++){
            if(i>mid) arr[k]=aux[j++];   //左边用尽
            else if(j>hi) arr[k]=aux[i++]; //右边用尽
            else if(aux[i]<aux[j]) arr[k]=aux[i++];
            else if(aux[j]<aux[i]) arr[k]=aux[j++];
            else{
                arr[k++]=aux[i++];
                arr[k]=aux[j++];
            }
        }
    }

    public static void sort(int arr[]){
        sort(arr,0,arr.length-1);
    }

    public static void sort(int arr[],int lo,int hi){
        if(lo>=hi)
            return;
        int mid=lo+(hi-lo)/2;
        sort(arr,lo,mid);
        sort(arr,mid+1,hi);
        merge(arr,lo,mid,hi);
    }
}
