package sort;

public class Selection {

    public static void sort(int[] a){
         int N=a.length;  //数组的长度
        for(int i =0;i<N;i++){
            int min=i;  //最小元素的索引
            for(int j=i+1;j<N;j++){
                if(a[j]<a[min])  //元素小于min索引处的元素时，将此元素位置赋值给索引
                    min=j;
            }
            //交换a[i]和a[min]
            int t =a[i];
            a[i]=a[min];
            a[min]=t;
        }
    }


    public static void main(String[] args) {
        int[] a={3,2,3,5,1,2,3,4,6,8};
        sort(a);
        for(int i :a ){
            System.out.println(i);
        }
    }
}
