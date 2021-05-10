package leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的前序遍历
 */
public class PreorderTraversal {

    //递归法
        public List<Integer> preorderTraversal(TreeNode root) {
            List<Integer> result=new ArrayList<>();
            traversal(root,result);
            return result;
        }

        public void traversal(TreeNode root,List<Integer> list){
            if(root==null)
                return;

            list.add(root.val);  //中
            traversal(root.left,list); //左
            traversal(root.right,list); //右

        }

    //迭代法
    public List<Integer> preorderTraversal2(TreeNode root) {
        Stack<TreeNode> stack=new Stack<TreeNode>();
        List<Integer> result= new ArrayList<>();

        //节点入栈
        stack.push(root);


        while (!stack.isEmpty()){
            TreeNode node=stack.pop();
            if(node!=null) {
                result.add(node.val);   //中
            }else continue;

            //栈时先进后出，所以压入右结点
            if(node.right!=null){
                stack.push(node.right);
            }
            //再压入左节点
            if(node.left!=null){
                stack.push(node.left);
            }
        }
        return result;
    }



}




