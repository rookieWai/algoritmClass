package leetcode;

/**
 * 203. 移除链表元素
 * 给你一个链表的头节点 head 和一个整数 val ，请你删除链表中所有满足 Node.val == val 的节点，并返回 新的头节点 。
 */

public class RemoveElements {
    //直接解法
    public ListNode removeElements1(ListNode head, int val) {
        //删除头结点
        while(head!=null&&head.val==val){
            head=head.next;
        }

        ListNode pointer=head; //用于删除操作的指针
        while(pointer!=null&&pointer.next!=null){
            //如果结点值等于val删除这个结点
            if(pointer.next.val==val){
                pointer.next=pointer.next.next;
            }
            else {
                pointer=pointer.next;  //指针向下一个节点移动
            }
        }
        //返回头结点
        return head;
    }

    //采用虚拟头结点
    public ListNode removeElements2(ListNode head, int val) {
        ListNode dummyHead=new ListNode(0); //创建虚拟头结点
        dummyHead.next=head;  //虚拟头结点的指针指向头结点

        ListNode pointer=dummyHead; //用于删除操作的指针
        // 删除结点
        while(pointer!=null&&pointer.next!=null){
            //如果结点值等于val删除这个结点
            if(pointer.next.val==val){
                pointer.next=pointer.next.next;
            }
            else {
                pointer=pointer.next;  //指针向下一个节点移动
            }
        }


        //返回实际头结点，虚拟头结点的下一个节点
        return dummyHead.next;
    }
}






class ListNode {
    int val;
    ListNode next;

    ListNode() {
    }

    ListNode(int val) {
        this.val = val;
        next=null;
    }

    ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }
}