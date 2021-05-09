package sort;

public class SelectionSort {
    public static void selectionSort(int arr[]){
        int n=arr.length;
        int temp,minIndex;
        for(int i=0;i<n-1;i++){
            minIndex=i;
            for(int j=i+1;j<n;j++){
                if(arr[minIndex]>arr[j])
                {
                    minIndex=j;
                }
            }
            if(minIndex!=i) {
                temp = arr[i];
                arr[i]=arr[minIndex];
                arr[minIndex]=temp;
            }
        }
    }
}
