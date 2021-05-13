package leetcode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 222. 完全二叉树的节点个数
 *给你一棵 完全二叉树 的根节点 root ，求出该树的节点个数。
 *
 * 完全二叉树 的定义如下：在完全二叉树中，除了最底层节点可能没填满外，其余每层节点数都达到最大值，
 * 并且最下面一层的节点都集中在该层最左边的若干位置。若最底层为第 h 层，则该层包含 1~ 2h 个节点。
 */

public class CountNodes {
    public int countNodes(TreeNode root) {
        return count(root);
    }

    //递归法，后序遍历
    public int count(TreeNode root){
        if(root==null) return 0;

        //左子树的节点个数
        int leftNodes=count(root.left);
        //右子树的节点个数
        int rightNodes=count(root.right);
        //左子树的节点个数加右子树的节点个数，再加根节点
        return leftNodes+rightNodes+1;

    }

    //迭代法，层序遍历
    public int countNodes2(TreeNode root) {
        if(root==null) return 0;
        Queue<TreeNode> queue=new LinkedList<>();
        queue.offer(root);
        int nums=0;

        while(!queue.isEmpty()){
            int size=queue.size();
            for(int i=0;i<size;i++){
                nums++;
                TreeNode node=queue.poll();
                if(node.left!=null){
                    queue.offer(node.left);
                }
                if(node.right!=null){
                    queue.offer(node.right);
                }
            }
        }
        return nums;
    }

}
