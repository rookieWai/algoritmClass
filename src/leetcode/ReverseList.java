package leetcode;

public class ReverseList {
    public ListNode reverseList(ListNode head) {
        ListNode nexthead;
        ListNode node=head;
        do{
            nexthead=head;
            node.next=nexthead;
        }while((node=node.next)!=null);

        return head=nexthead;
    }
}
