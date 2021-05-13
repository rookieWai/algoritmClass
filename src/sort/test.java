package sort;


import java.util.Arrays;

public class test {    public static void main(String[] args) {
        int[] a={5,3,1,2,4,3,9,4,6,7};
        RadixSort.sort(a);
        System.out.println(Arrays.toString(a));
    }

}
