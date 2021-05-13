package leetcode;

/**
 * 110. 平衡二叉树
 *
 * 给定一个二叉树，判断它是否是高度平衡的二叉树。
 * 本题中，一棵高度平衡二叉树定义为：
 * 一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1 。
 */
public class IsBalanced {

    public boolean isBalanced(TreeNode root) {
        return getHeight(root)==-1?false:true;
    }

    //如果以该节点为根节点的数平衡二叉树则返回树的高度，不是返回-1
    private int getHeight(TreeNode root){
        if(root==null) return 0;

        //左子树根节点的高度
        int leftHeight=getHeight(root.left);
        //如果左子树不是平衡二叉树，返回-1
        if(leftHeight==-1) return -1;
        //右子树根节点的高度
        int rightHeight=getHeight(root.right);
        //如果右子树不是平衡二叉树，返回-1
        if(rightHeight==-1) return -1;

        //左右子树的高度差绝对值大于1返回-1，否则返回左右子树最大高度再加上根节点
        return Math.abs(leftHeight-rightHeight)>1?-1:1+Math.max(leftHeight,rightHeight);

    }
}
