package leetcode;

/**
 * 226. 翻转二叉树
 */

public class InvertTree {
    //前序遍历解决问题
    public TreeNode invertTree(TreeNode root) {
        if(root==null) return null;
        //反转左右孩子
        TreeNode temp=root.left;
        root.left=root.right;
        root.right=temp;
        invertTree(root.left); //左
        invertTree(root.right); //右
        return root;
    }
}
