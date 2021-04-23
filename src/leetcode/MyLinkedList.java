package leetcode;

/**
 * 设计链表的实现。您可以选择使用单链表或双链表。单链表中的节点应该具有两个属性：val和next。val是当前节点的值，next是指向下一个节点的指针/引用。如果要使用双向链表，则还需要一个属性prev以指示链表中的上一个节点。假设链表中的所有节点都是 0-index 的。
 *
 * 在链表类中实现这些功能：
 *
 * get(index)：获取链表中第index个节点的值。如果索引无效，则返回-1。
 * addAtHead(val)：在链表的第一个元素之前添加一个值为val的节点。插入后，新节点将成为链表的第一个节点。
 * addAtTail(val)：将值为val 的节点追加到链表的最后一个元素。
 * addAtIndex(index,val)：在链表中的第index个节点之前添加值为val 的节点。如果index等于链表的长度，则该节点将附加到链表的末尾。如果 index 大于链表长度，则不会插入节点。如果index小于0，则在头部插入节点。
 * deleteAtIndex(index)：如果索引index 有效，则删除链表中的第index 个节点。
 *
 *
 */


class MyLinkedList {

    //链表大小
    int size;
    //链表的虚拟头结点
    ListNode dummyHead;


    public MyLinkedList() {
        size=0;
        dummyHead=new ListNode(0);
    }

    //取链表中第index个节点的值。如果索引无效，则返回-1。
    public int get(int index) {
        //索引大于等于链表大小，或者小于零，无效返回-1
        if(index>=size||index<0){
            return -1;
        }
        //定一个指针执行头结点
        ListNode cur=dummyHead.next;
        //指针移动到index位置的结点上
        while(index-->0){
            cur=cur.next;
        }
        return cur.val;
    }

    //在链表的第一个元素之前添加一个值为val的节点。插入后，新节点将成为链表的第一个节点。
    public void addAtHead(int val) {
        //创建值为val的新结点
        ListNode newNode=new ListNode(val);
        //将新结点的指针指向虚拟头结点的下一个结点，即头结点
        newNode.next=dummyHead.next;
        //讲虚拟头结点的指针指向新结点
        dummyHead.next=newNode;
        //链表长度加1
        size++;
    }

    //将值为val 的节点追加到链表的最后一个元素。
    public void addAtTail(int val) {
        //创建值为val的新结点
        ListNode newNode=new ListNode(val);
        //创建指针指向虚拟头结点
        ListNode cur=dummyHead;
        //通过循环，将指针移动到尾结点
        while(cur.next!=null){
            cur=cur.next;
        }
        //将新结点追加到尾结点后面
        cur.next=newNode;
        //链表长度加1
        size++;
    }

    //在链表中的第index个节点之前添加值为val 的节点。如果index等于链表的长度，则该节点将附加到链表的末尾。如果 index 大于链表长度，则不会插入节点。如果index小于0，则在头部插入节点。
    public void addAtIndex(int index, int val) {
        //先判断index是否大于size，大于直接退出。
        if(index>size){
            return;
        }
        //创建值为val的新结点
        ListNode newNode=new ListNode(val);
        //创建指针指向头结点
        ListNode cur=dummyHead;
        //移动指针到index的前一个，因为我们是从虚拟头结点开始的，所以移动index步即是index所以位置的前一个结点
        while(index-->0){
            cur=cur.next;
        }
        //插入结点
        newNode.next=cur.next;
        cur.next=newNode;
        size++;
    }

    //如果索引index 有效，则删除链表中的第index 个节点。
    public void deleteAtIndex(int index) {
        ListNode cur=dummyHead;
        //移动指针指向索引index前面的结点索引
        if(index>=0&&index<size){
            while (index-->0){
                cur=cur.next;
            }
            //删除操作，将删除位置前面结点的指针指向删除位置后面的结点
            cur.next=cur.next.next;
            //链表大小-1
            size--;
        }
    }
}
