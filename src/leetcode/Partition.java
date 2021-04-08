package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是 回文串 。返回 s 所有可能的分割方案。
 *回文串 是正着读和反着读都一样的字符串。
 *
 *
 */

public class Partition {


    List<String> path=new ArrayList<>();
    List<List<String>> result=new ArrayList<>();


    public List<List<String>> partition(String s) {
        backTracking(s,0);
        return result;

    }


    public void backTracking(String s,int startIndex){

        //切割到字符串末尾，终止本次递归，保存结果
        if(startIndex>=s.length()){
            result.add(new ArrayList<>(path));
        }

        //通过startIndex判断下次开始切割的起始位置
        for(int i=startIndex;i<s.length();i++){
            //判断字符串的[startIndex,i]子串是否为回文，是进行切割
            if(isPalindrome(s,startIndex,i)){
                String str=s.substring(startIndex,i+1 );   //获取切割的子串
                path.add(str);  //保存切割的子串
            }else {   //不是回文跳出本次循环，继续下次
                continue;
            }
            backTracking(s,i+1);  //递归调用，下一次切割从前一个切割的后一个元素开始
            path.remove(path.size()-1);  //回溯
        }
    }


    //回文判断
    public boolean isPalindrome(String s,int start,int end){
        //start从左到右，end从右到左，判断前后是否一致
        for(int i=start,j=end;i<j;i++,j--){
            if(s.charAt(i)!=s.charAt(j)){
                return false;
            }
        }
        return true;
    }

}
