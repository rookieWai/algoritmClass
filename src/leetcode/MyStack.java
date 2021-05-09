package leetcode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 225. 用队列实现栈
 *
 * 请你仅使用两个队列实现一个后入先出（LIFO）的栈，并支持普通队列的全部四种操作（push、top、pop 和 empty）。
 */

class MyStack {

    //队列1，实现栈操作的队列
    Queue<Integer> queue1;
    //队列2，用于保存数据、对数据进行交换的队列
    Queue<Integer> queue2;

    /** Initialize your data structure here. */
    public MyStack() {
        queue1=new LinkedList();
        queue2=new LinkedList();
    }

    /** Push element x onto stack. */
    public void push(int x) {

        //元素进入队列2
        queue2.offer(x);

        //队列1所有元素出队，并进入队列2
        while (!queue1.isEmpty()) {
            queue2.offer(queue1.poll());
        }

        //将队列1和队列2互换
        Queue<Integer> temp = queue1;
        queue1 = queue2;
        queue2 = temp;

    }

    /** Removes the element on top of the stack and returns that element. */
    public int pop() {
        return queue1.poll();
    }

    /** Get the top element. */
    public int top() {
        return queue1.peek();
    }

    /** Returns whether the stack is empty. */
    public boolean empty() {
        return queue1.isEmpty();
    }
}
