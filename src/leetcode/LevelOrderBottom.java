package leetcode;

import java.util.*;

/**
 * 107. 二叉树的层序遍历 II
 * 给定一个二叉树，返回其节点值自底向上的层序遍历。 （即按从叶子节点所在层到根节点所在的层，逐层从左向右遍历）
 */

public class LevelOrderBottom {
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        Queue<TreeNode> queue=new LinkedList<>();
        List<List<Integer>> result=new ArrayList<>();
        if(root!=null) queue.offer(root);

        while(!queue.isEmpty()){
            int size=queue.size();
            List<Integer> list=new ArrayList<>();

            for(int i=0;i<size;i++){
                TreeNode node=queue.poll();
                list.add(node.val);
                if(node.left!=null) queue.offer(node.left);
                if(node.right!=null) queue.offer(node.right);

            }
            result.add(list);
        }
        Collections.reverse(result);
        return result;
    }
}
