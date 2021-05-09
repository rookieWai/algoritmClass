package sort;

public class InsertionSort {
    public static void insertionSort(int arr[]){

        int n=arr.length;

        for(int i=1;i<n;i++){
            for(int j=i;j> 0&&arr[j-1]>arr[j];j--){
                int temp=arr[j];
                arr[j]=arr[j-1];
                arr[j-1]=temp;
            }
        }
    }
}
