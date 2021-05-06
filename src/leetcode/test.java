package leetcode;

public class test {
    public static void main(String[] args) {
        EvalRPN evalRPN=new EvalRPN();

        System.out.print(evalRPN.evalRPN( new String[]{"2", "1", "+", "3", "*"}));
    }
}
