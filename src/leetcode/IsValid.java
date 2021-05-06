package leetcode;
/**
 * 20. 有效的括号
 *
 * 给定一个只包括 '('，')'，'{'，'}'，'['，']'的字符串 s ，判断字符串是否有效。
 *
 * 有效字符串需满足：
 * 左括号必须用相同类型的右括号闭合。
 * 左括号必须以正确的顺序闭合。
 *
 */


import java.util.Stack;

public class IsValid {
    public boolean isValid(String s) {

        Stack<Character> stack=new Stack();

        for(int i=0;i<s.length();i++){
            //压入对应匹配的右括号
            if(s.charAt(i)=='(')  stack.push(')');
            else if(s.charAt(i)=='{')  stack.push('}');
            else if(s.charAt(i)=='[') stack.push(']');
            //栈为空或者没有匹配的字符括号
            else if(stack.isEmpty()||stack.peek()!=s.charAt(i)) return false;
            //stack.top与s.charAt(i)相等时，弹出元素
            else stack.pop();
        }

        //栈为空返回ture，不为空返回false
        return stack.isEmpty();

    }
}
