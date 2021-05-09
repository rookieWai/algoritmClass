package leetcode;


/**
 * 1047. 删除字符串中的所有相邻重复项
 * 给出由小写字母组成的字符串S，重复项删除操作会选择两个相邻且相同的字母，并删除它们。
 *
 * 在 S 上反复执行重复项删除操作，直到无法继续删除。
 *
 * 在完成所有重复项删除操作后返回最终的字符串。答案保证唯一。
 *
 *

 */

import java.util.Stack;

public class RemoveDuplicates {
    public int removeDuplicates(int[] nums) {
        int num=0;
        int n=nums.length;

        for(int i=0;i<n;i++){
            num++;
            if(nums[i]!=nums[i+1]){
                while(num-->2){
                    remove(nums,n,i-num);
                    i--;
                    n--;
                }
                num=0;
            }
        }
        return n;
    }

    public void remove(int[] nums,int length,int index){
        for(int i=index;i<length-1;i++){
            int temp=nums[i];
            nums[i]=nums[i+1];
            nums[i+1]=nums[i];
        }
    }

    //使用栈
    public String removeDuplicates(String S) {
        Stack<Character> stack=new Stack();

        for(int i=0;i<S.length();i++){
            //判断是否有相邻重复项
            if(!stack.isEmpty()&&stack.peek().equals(S.charAt(i))){
                stack.pop();
            }
            else{
                stack.push(S.charAt(i));
            }
        }


        StringBuilder result=new StringBuilder();
        while(!stack.isEmpty()){
            result.append(stack.pop());
        }
        return result.reverse().toString();
    }
}
