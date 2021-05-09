package leetcode;

import java.util.*;

/**
 * 二叉树的后序遍历
 */
public class PostorderTraversal {
    public List<Integer> postorderTraversal(TreeNode root) {
        List<Integer> result=new ArrayList<>();
        traversal(root,result);
        return result;
    }

    public void traversal(TreeNode root,List<Integer> list){
        if(root==null)
            return;

        traversal(root.left,list); //左
        traversal(root.right,list); //右
        list.add(root.val);  //中
    }


    //迭代法
    public List<Integer> postorderTraversal2(TreeNode root) {
        Stack<TreeNode> stack=new Stack<TreeNode>();
        List<Integer> result= new ArrayList<>();

        //结点入栈
        stack.push(root);


        while (!stack.isEmpty()){
            TreeNode node=stack.pop();
            if(node!=null) {
                result.add(node.val);
            }else continue;

            //

            if(node.left!=null){
                stack.push(node.left);
            }

            if(node.right!=null){
                stack.push(node.right);
            }
        }
        Collections.reverse(result);
        return result;
    }
}
