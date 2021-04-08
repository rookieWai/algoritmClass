package sort.mergesort;

public class MergeBottomUp {
    public static void sort(int[] a){
        int N=a.length;
        for(int sz=1;sz<N;sz=sz+sz)     //sz子数组大小
            for(int lo=0;lo<N-sz;lo+=sz+sz) //lo:子数组索引
                Merge.merge(a,lo,lo+sz-1,Math.min(lo+sz+sz-1,N-1));
    }
}
