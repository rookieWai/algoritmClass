package leetcode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 二叉树的中序遍历
 */
public class InorderTraversal {

    //递归法
    public List<Integer> inorderTraversal(TreeNode root) {
        List<Integer> result=new ArrayList<>();
        traversal(root,result);
        return result;
    }

    public void traversal(TreeNode root,List<Integer> list){
        if(root==null)
            return;

        traversal(root.left,list); //左
        list.add(root.val);  //中
        traversal(root.right,list); //右

    }

    //迭代法
    public List<Integer> inorderTraversal2(TreeNode root) {
        Stack<TreeNode> stack=new Stack<TreeNode>();
        List<Integer> result= new ArrayList<>();
        TreeNode pointer=new TreeNode();

         pointer=root;


        while(pointer!=null||!stack.isEmpty()){
            //访问到最左边的结点
            if(pointer!=null){
                stack.push(pointer);
                pointer=pointer.left;
            }else {
                pointer=stack.pop();
                result.add(pointer.val);
                pointer=pointer.right;
            }
        }
        return result;

    }


}
