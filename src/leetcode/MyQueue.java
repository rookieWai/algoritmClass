package leetcode;

/**
 * 232. 用栈实现队列
 * 请你仅使用两个栈实现先入先出队列。队列应当支持一般队列支持的所有操作（push、pop、peek、empty）：
 */

import java.util.Stack;

class MyQueue {

    Stack<Integer> stackIn; //输入栈
    Stack<Integer> stackOut; //输出栈

    /** Initialize your data structure here. */
    public MyQueue() {
        stackIn=new Stack();
        stackOut=new Stack();
    }

    /** Push element x to the back of queue. */
    public void push(int x) {
        stackIn.push(x);
    }

    /** Removes the element from in front of queue and returns that element. */
    public int pop() {
        if(stackOut.isEmpty()){
            while (!stackIn.isEmpty()){
                stackOut.push(stackIn.pop());
            }
        }
        return  stackOut.pop();
    }

    /** Get the front element. */
    public int peek() {
        if(stackOut.isEmpty()){
            while (!stackIn.isEmpty()){
                stackOut.push(stackIn.pop());
            }
        }
        return  stackOut.peek();
    }

    /** Returns whether the queue is empty. */
    public boolean empty() {
        return stackOut.isEmpty() && stackIn.isEmpty();
    }
}


