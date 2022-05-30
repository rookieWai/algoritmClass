package leetcode;

import java.util.Collections;

/**
 * 404. 左叶子之和
 * 计算给定二叉树的所有左叶子之和。
 */
public class SumOfLeftLeaves {

    public int sumOfLeftLeaves(TreeNode root){

        if(root==null){
            return 0;
        }

        int x=0;


        if(root.left!=null&&root.left.left==null&&root.left.right==null){
            x=root.left.val;
        }


        return x+sumOfLeftLeaves(root.left)+sumOfLeftLeaves(root.right);
    }
}
