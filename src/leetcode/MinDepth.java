package leetcode;

/**
 * 给定一个二叉树，找出其最小深度。
 *
 * 最小深度是从根节点到最近叶子节点的最短路径上的节点数量。
 *
 * 说明：叶子节点是指没有子节点的节点。
 */

public class MinDepth {
    public int minDepth(TreeNode root) {
        return min(root);
    }

    public int min(TreeNode root){

        if(root==null) return 0;

        int leftMin=min(root.left);   //左子树的深度
        int rightMin=min(root.right); //右子树的深度

        //如果右子树为null，那么返回的深度应该是左子树的深度加根节点1
        if(root.right==null&&root.left!=null){
            return leftMin+1;
        }

        //如果左子树为null，那么返回的深度应该是右子树的深度加根节点1
        if(root.right!=null&&root.left==null){
            return rightMin+1;
        }

        //取左子树和右子树最小深度的，加上根节点的1
        return 1+Math.min(leftMin,rightMin);

    }
}
