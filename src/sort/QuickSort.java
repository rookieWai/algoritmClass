package sort;

public class QuickSort {

    public static void sort(int arr[]){
        sort(arr,0,arr.length-1);
    }

    public static void sort(int arr[],int lo,int hi){
        if(hi<=lo)
            return;
        //返回切割数
        int j=partition(arr,lo,hi);
        sort(arr,lo,j-1);
        sort(arr,j+1,hi);
    }

    private static int  partition(int arr[],int lo,int hi){
        int i=lo;
        int j=hi+1;

        int v=arr[lo];
        while(true){
            while(arr[++i]<v){
                if(i==hi)
                    break; }
            while(arr[--j]>v){
                if(j==lo)
                    break; }
            if(i>=j)
                break;
            swap(arr,i,j);
        }
        swap(arr,lo,j);
        return j;
    }

    private static void swap(int arr[],int i,int j){
        int temp=arr[i];
        arr[i]=arr[j];
        arr[j]=temp;
    }
}
