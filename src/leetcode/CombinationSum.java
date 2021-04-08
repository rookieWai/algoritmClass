package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 组合总和
 * 问题：
 * 给定一个无重复元素的数组candidates和一个目标数target，找出candidates中所有可以使数字和为target的组合。
 * candidates中的数字可以无限制重复被选取。
 * 说明：
 * 所有数字都是正整数。
 * 解集不能包含重复的组合。
 *
 * 分析：
 *
 */


public class CombinationSum {


    List<Integer> path=new ArrayList<>();
    List<List<Integer>> result=new ArrayList<>();

    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        //排序后进行剪枝操作
        Arrays.sort(candidates);
       backTracking1(candidates,target,0,0);
       return result;
    }


    //未剪枝
    public void backTracking1(int[] candidates,int target,int sum,int startIndex){
        //终止本次递归，不做任何操作
        if(sum>target){
            return;
        }
        //保存结果
        if(sum==target){
            result.add(new ArrayList<Integer>(path));
            return;
        }
        //通过startIndex通知递归的下次开始原始为自己
        for(int i=startIndex;i<candidates.length&&sum+candidates[i]<=target;i++){
            path.add(candidates[i]);  //保存节点值
            sum+=candidates[i];       //节点值做加
            backTracking1(candidates,target,sum,i); //递归
            sum-=candidates[i];                 //回溯
            path.remove(path.size()-1);   //回溯
        }
    }


    //剪枝
    public void backTracking2(int[] candidates,int target,int sum,int startIndex){

        if(sum>target){
            return;
        }

        if(sum==target){
            result.add(new ArrayList<Integer>(path));
            return;
        }

        for(int i=startIndex;sum+candidates[i]<=target&&i<candidates.length;i++){
            path.add(candidates[i]);
            sum+=candidates[i];
            backTracking2(candidates,target,sum,i);
            path.remove(path.size()-1);
        }
    }

}
