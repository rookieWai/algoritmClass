package leetcode;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 题目：组合
 * 给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。
 *
 * （1）确定递归函数的参数和返回值
 * n k startIndex代表我们遍历的起始位置，每次递归时我们都需要传入起始位置
 * （2）确定终止条件
 * 当我们收集到k个数时，保存这个集合，返回
 */

public class Combine {

    List<Integer> path=new ArrayList<>();
    List<List<Integer>> result=new ArrayList<>();

    public List<List<Integer>> combine(int n, int k) {
      backTracking(n,k,1);
      return result;
    }

    public void backTracking(int n, int k,int startIndex) {

         if(path.size()==k){
             result.add(new ArrayList<Integer>(path));
             return;
         }

         for (int i=startIndex;i<=n-(k-path.size())+1;i++) {
             path.add(i);
             backTracking(n, k, i + 1);
             path.remove(path.size() - 1);
         }
    }
}
