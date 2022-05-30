package leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 513. 找树左下角的值
 * 给定一个二叉树，在树的最后一行找到最左边的值。
 *
 * 使用迭代法，层序遍历
 */

public class FindBottomLeftValue {    public int findBottomLeftValue(TreeNode root){

        if(root==null) return 0;
        Queue<TreeNode> queue=new LinkedList();
        queue.offer(root);
        int result=0;

        while(!queue.isEmpty()){
            int size= queue.size();
            for (int i = 0; i < size; i++) {
                TreeNode node=queue.poll();
                if(i==0) result=node.val;  //记录最后一行最左边元素值
                if(node.left!=null){
                    queue.offer(node.left);
                }
                if(node.right!=null){
                    queue.offer(node.right);
                }
            }
        }

        return result;
    }

}
