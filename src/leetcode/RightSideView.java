package leetcode;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 199. 二叉树的右视图
 * 给定一棵二叉树，想象自己站在它的右侧，按照从顶部到底部的顺序，返回从右侧所能看到的节点值。
 */
public class RightSideView {
    public List<Integer> rightSideView(TreeNode root) {
        Queue<TreeNode> queue=new LinkedList<>();
        List<Integer> result=new ArrayList<>();
        if(root!=null) queue.offer(root);

        while(!queue.isEmpty()){
            int size=queue.size();
            List<Integer> list=new ArrayList<>();

            for(int i=0;i<size;i++){
                TreeNode node=queue.poll();
                if(i==size-1){
                    result.add(node.val);
                }
                if(node.left!=null) queue.offer(node.left);
                if(node.right!=null) queue.offer(node.right);
            }
        }
        return result;
    }

}
