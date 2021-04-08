package sort;

/**
 * 冒泡排序
 * 每一趟都比较两个元素，如果前面的元素比后面的元素小，则交换，每跑完一趟，最大的元素就到了末尾
 * 第一趟就是n个元素，比较n-1次，然后最大的元素就到了最后面
 * 第二趟就是前面的n-1个元素，比较n-2次，再把最大的元素放大最后面
 * 这样持续下去，到n-1趟就形成了有序数列
 */
public class Bubble {

    public static void sort(int[] a){
        int n=a.length;
        for(int i=0;i<n-1;i++){
            for(int j=0;j<n-i-1;j++){
                //前一个元素大于后一个元素时，交换
                if(a[j]>a[j+1]){
                    int temp=a[j];
                    a[j]=a[j+1];
                    a[j+1]=temp;
                }
            }
        }
    }
}
