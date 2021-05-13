package sort;

public class QuickSort {

    public static void sort(int arr[]){
        sort(arr,0,arr.length-1);
    }

    public static void sort(int arr[],int lo,int hi){
        if(hi<=lo)
            return;
        //返回切割数位置
        int j=partition(arr,lo,hi);
        //将切割数左边部分排序
        sort(arr,lo,j-1);
        //将切割数右边部分排序
        sort(arr,j+1,hi);
    }

    //切分操作
    private static int  partition(int arr[],int lo,int hi){
        //左右扫描指针
        int i=lo;
        int j=hi+1;

        //切分元素
        int v=arr[lo];

        while(true){
            //从左到右扫描，找到大于切分元素的数
            while(arr[++i]<v){
                if(i==hi)
                    break; }
            //从右到左扫描，找到小于切分元素的数
            while(arr[--j]>v){
                if(j==lo)
                    break; }
            if(i>=j)
                break;
            //找到后，交换两个元素的位置
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
