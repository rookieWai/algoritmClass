package leetcode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 对称二叉树
 */
public class IsSymmetric {
    public boolean isSymmetric(TreeNode root) {
        if(root==null) return true;
        return compare(root.right,root.left);
    }

    private boolean compare(TreeNode left,TreeNode right){
        if(left!=null&&right==null) return false;  //左节点为空，右节点不为空，不对称
        else if(left==null&&right!=null) return false; //右节点为空，左节点不为空，不对称
        else if(left==null&&right==null) return true; //左右都空，对称
        else if(left.val!=right.val) return false; //左右节点不为空，但是值不同，不对称
        return compare(left.left,right.right)&&compare(left.right,right.left);  //递归操作，做内外比较

    }

    //迭代法，使用队列
    public boolean isSymmetric2(TreeNode root) {
        if(root==null) return true;
        Queue<TreeNode> queue=new LinkedList<>();
        TreeNode left=root.left;
        TreeNode right=root.right;
        queue.offer(left);
        queue.offer(right);


        while(!queue.isEmpty()){
            left=queue.poll();
            right=queue.poll();
            //左右节点都为空，跳出本次循环，继续下次
            if(left==null&&right==null){
                continue;
            }
            //当左节点为空或者右节点为空或者左右节点的值不等时，表示不对称返回false;
            if (left==null||right==null||left.val!=right.val){
                return false;
            }

            //都不为空且值相同，将各自子节点入队
            queue.offer(left.left);
            queue.offer(right.right);
            queue.offer(left.right);
            queue.offer(right.left);
        }
        return true;
    }
}
