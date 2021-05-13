package leetcode;
/**
 * 257. 二叉树的所有路径
 *
 * 给定一个二叉树，返回所有从根节点到叶子节点的路径。
 * 说明: 叶子节点是指没有子节点的节点。
 */

import java.util.ArrayList;
import java.util.List;


public class BinaryTreePaths {
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result=new ArrayList<>();
        paths(root,"",result);
        return result;
    }


    public void paths(TreeNode root, String path, List<String> result){
        if(root!=null){
            StringBuffer paths=new StringBuffer(path);
            paths.append(root.val);  //中
            //如果到达叶子节点，将该路径放入结果
            if(root.right==null&&root.left==null){
                result.add(paths.toString());
            }else {
                paths.append("->");
                paths(root.left,paths.toString(),result);  //左
                paths(root.right,paths.toString(),result); //右
            }
        }
    }


}
