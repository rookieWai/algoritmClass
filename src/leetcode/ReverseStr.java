package leetcode;

/**
 *541. 反转字符串 II
 给定一个字符串 s 和一个整数 k，你需要对从字符串开头算起的每隔 2k 个字符的前 k 个字符进行反转。
 如果剩余字符少于 k 个，则将剩余字符全部反转。
 如果剩余字符小于 2k 但大于或等于 k 个，则反转前 k 个字符，其余字符保持原样。

 */

public class ReverseStr {
    //实现字符串的反转
    public void reverse(char []s,int start,int end){
        for(int i=start,j=end;i<j;i++,j--){
            char temp=s[i];
            s[i]=s[j];
            s[j]=temp;
        }
    }
    public String reverseStr(String s, int k) {
        char[] a=s.toCharArray();
        //每次移动2k个位置
        for(int i=0;i<s.length();i=i+2*k){
            //剩下的元素大于等于k时
            if(i+k<=a.length){
                reverse(a,i,i+k-1);
                continue;
            }
            //剩下的元素小于k时
            reverse(a,i, a.length-1);
        }

        return new String(a);
    }
}