package sort;

public class HeapSort {
    //对i结点以下堆化
    private static void heap(int tree[],int n,int i){
        int c1=i*2+1;
        int c2=i*2+2;
        int max=i;
        if(c1<n&&tree[c1]>tree[max]){
            max=c1;
        }
        if(c2<n&&tree[c2]>tree[max]){
            max=c2;
        }

        if(max!=i){
            swap(tree,max,i);
            heap(tree,n,max);
        }
    }

    //构建堆
    private static void buildHeap(int tree[],int n){
        int lastNode=n-1;
        int parent=(lastNode-1)/2;

        for(int i=parent;i>=0;i--){
            heap(tree,n,i);
        }
    }

    //整体实现过程，并实现移除操作
    public static void sort(int tree[]){
        int n=tree.length;
        //构建堆
        buildHeap(tree,n);

        //移除最大元素，继续堆化
        for(int i=n-1;i>=0;i--){
            swap(tree,i,0);
            heap(tree,i,0);
        }
    }

    private static void swap(int arr[],int i,int j){
        int temp=arr[i];
        arr[i]=arr[j];
        arr[j]=temp;
    }
}
