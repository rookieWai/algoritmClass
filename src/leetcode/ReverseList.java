package leetcode;

import java.util.List;

class Solution {
    public ListNode reverseList(ListNode head) {
        return reverse(null,head);
    }


    public ListNode reverse(ListNode pre,ListNode post){
        if(post==null){
            return pre;
        }

        ListNode temp=post.next; //记录post的下一个结点
        post.next=pre;

        return reverse(post,temp);
    }

    public ListNode reverseList2(ListNode head){
        //两相邻节点
        ListNode post=head;  //后面的节点，从头结点开始
        ListNode pre=null;   //前面的结点，后面的结点从头结点开始，所以前面的结点从null开始
        ListNode temp; //后面的结点指针要指向前面的结点，所以用来保存后面结点再后面的结点

        while(post!=null){
            temp=post.next; //先记录后面结点再后面的结点
            post.next=pre; //两相邻结点指针反转
            //两相邻结点同时向后移动，继续反正下一个指针
            pre=post;
            post=temp;
        }
        //反转后最后的结点就是头结点，返回头结点
        return pre;
    }
}