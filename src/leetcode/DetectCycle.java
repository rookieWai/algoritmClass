package leetcode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 142.环形链表
 *
 * 给定一个链表，返回链表开始入环的第一个节点。如果链表无环，则返回null。
 *
 * 为了表示给定链表中的环，我们使用整数 pos 来表示链表尾连接到链表中的位置（索引从 0 开始）。 如果 pos 是 -1，则在该链表中没有环。注意，pos 仅仅是用于标识环的情况，并不会作为参数传递到函数中。
 *
 * 说明：不允许修改给定的链表。
 *
 * 进阶：
 * 你是否可以使用 O(1) 空间解决此题？

 */
public class DetectCycle {


    public ListNode detectCycle(ListNode head) {
        ListNode pos=head;
        //存放节点的哈希表
        Set<ListNode> hashTable=new HashSet<>();
        //遍历链表
        while(pos!=null){
            //判断当前结是否已经存在与集合中，是则有环，当前结点为入环结点
            if(hashTable.contains(pos)){
                return pos;
            }else {
                //不存在，加入集合中
                hashTable.add(pos);
            }
            //向下一个结点移动
            pos=pos.next;
        }
        //没环返回null
        return null;
    }

    public ListNode detectCycle2(ListNode head) {
        //定义两个指针，一快一慢，初始都指向头结点
        ListNode slow=head;
        ListNode fast=head;

        //开始移动指针
        while(fast!=null&&fast.next!=null){
            //慢指针每次移动一步
            slow=slow.next;
            //快指针每次移动两步
            fast= fast.next.next;
            //当两个指针指向同一个结点时，相遇，则有环，找入口结点
            if(slow==fast){
                //定义两个指针，一个指向头，一个指向相遇点
                ListNode indexA=head;
                ListNode indexB=slow;
                //两指针每次同时移动一个单位，相遇时的结点就是入口结点
                while(indexA!=indexB){
                    indexA=indexA.next;
                    indexB=indexB.next;
                }
                //返回入口结点
            return indexA;
            }
        }
        //不存在环，返回null
        return null;
    }

}
