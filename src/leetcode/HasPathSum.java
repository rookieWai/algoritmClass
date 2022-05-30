package leetcode;

/**
 * @ClassName HasPathSum
 * @Author Bcw
 * @Date 2021/7/5 20:14
 *
 * 112. 路径总和
 * 给你二叉树的根节点root 和一个表示目标和的整数argetSum ，判断该树中是否存在 根节点到叶子节点 的路径，这条路径上所有节点值相加等于目标和targetSum 。
 *

 */
public class HasPathSum {



    public boolean hasPathSum(TreeNode root, int targetSum) {
        if(root==null) return false;
        return traversal(root,targetSum-root.val);
    }

    private boolean traversal(TreeNode root,int count){

        if(root.left==null&&root.right==null&&count==0) return true;
        if(root.left==null&&root.right==null) return false;

        if(root.left!=null){
            if(traversal(root.left,count-root.left.val)) return true;
        }


        if(root.right!=null){
            if(traversal(root.right,count-root.right.val)) return true;
        }

        return false;
    }


}
