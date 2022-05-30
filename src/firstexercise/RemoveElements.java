package firstexercise;

/**
 * @ClassName RemoveElements
 * @Author Bcw
 * @Date 2022/5/30 21:57
 */
public class RemoveElements {
    //普通解法
    public ListNode removeElements(ListNode head,int val) {
        //删除头节点
        while (head != null && head.val==val){
            head=head.next;
        }

        //用于删除操作的指针
        ListNode pointer=head;
        //删除其它节点
        while (pointer != null && pointer.next !=null ) {
            if (pointer.next.val == val){
                //删除该节点
                pointer.next = pointer.next.next;
            }else {
                //移动节点
                pointer = pointer.next;
            }
        }
        return head;
    }

    //虚节点 dummyHead
    public ListNode removeElements2(ListNode head,int val) {
        ListNode dummyHead = new ListNode(0,head);
        ListNode pointer=dummyHead;
        //删除其它节点
        while (pointer != null && pointer.next !=null ) {
            if (pointer.next.val == val){
                //删除该节点
                pointer.next = pointer.next.next;
            }else {
                //移动节点
                pointer = pointer.next;
            }
        }
        return dummyHead.next;
    }
}
