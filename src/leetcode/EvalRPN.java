package leetcode;

import java.util.Stack;

/**
 * 150. 逆波兰表达式求值
 *根据逆波兰表示法，求表达式的值。
 *有效的算符包括 +、-、*、/ 。每个运算对象可以是整数，也可以是另一个逆波兰表达式。
 *
 *
 * 输入：tokens = ["2","1","+","3","*"]
 * 输出：9
 * 解释：该算式转化为常见的中缀算术表达式为：((2 + 1) * 3) = 9
 */
public class EvalRPN {


    public int evalRPN(String[] tokens) {

        Stack<Integer> stack=new Stack<Integer>();

        for(int i=0;i<tokens.length;i++){
            //字符为运算符时做运算处理
            if(tokens[i].equals("+")||tokens[i].equals("-")||tokens[i].equals("*")||tokens[i].equals("/")){
                //弹出两个数字
                int num1=stack.pop();
                int num2=stack.pop();
                //做运算，再把运算结果压入栈中
                switch(tokens[i]){
                    case "+":
                        stack.push(num2+num1);
                        break;
                    case "-":
                        stack.push(num2-num1);
                        break;
                    case "*":
                        stack.push(num2*num1);
                        break;
                    case "/":
                        stack.push(num2/num1);
                        break;
                    default:

                }
            }else {
                //字符为数子时压入
                stack.push(Integer.parseInt(tokens[i]));
            }
        }
        return stack.pop();
    }
}
