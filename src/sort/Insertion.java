package sort;

public class Insertion {

    public static void sort(int[] a){

        int N=a.length;  //数组长度
        for(int i=1 ; i<N;i++){
            for(int j=i;j>0&&a[j]<a[j-1];j--){
                //比较，待插入元素比前面的元素小时交换
                int t= a[j];
                a[j]=a[j-1];
                a[j-1]=t;
            }
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
