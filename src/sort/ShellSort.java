package sort;

public class ShellSort {
    public static void shellSort(int arr[]){
        for(int gap=arr.length/2;gap>0;gap/=2){
            for(int i=gap;i<arr.length;i++){
                int j=i;
                while(j-gap>=0&&arr[j]<arr[j-gap]){
                    int temp=arr[j];
                    arr[j]=arr[j-gap];
                    arr[j-gap]=temp;
                    j-=gap;
                }
            }
        }
    }
}
