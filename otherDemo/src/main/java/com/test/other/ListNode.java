package com.test.other;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2019/3/21
 * @modifyDate: 13:46
 * @Description:
 */
public class ListNode {

    int cal;
    ListNode next;

    public ListNode(int cal) {
        this.cal = cal;
    }

    public ListNode() {
    }

    public static void main(String[] args) {
        ListNode node = new ListNode(0);
        new ListNode().getListNode(node,19);

//        System.out.println(new ListNode().findNode(node,4).cal);
        new ListNode().reverseList(node);

    }

    private void getListNode(ListNode node,int i) {
        if (i == 0) return ;
        node.next = new ListNode(20-i);
        i--;
        getListNode(node.next,i);
    }


    public ListNode findNode(ListNode head,int k) {
        ListNode p1 = head;
        ListNode p2 = head;

        int a = k;
        int count = 0;

        while (p1 != null) {
            p1 = p1.next;
            count++;
            if (k<1)
                p2 = p2.next;
            k--;
        }
        if (count < a) return null;
        return p2;
    }


    public void reverseList(ListNode head) {
        ListNode next = null;
        ListNode pre = null;
        while (head != null) {
            next  = head.next;
            head.next = pre;
            pre = head;
            head = next;
        }

        while (pre != null) {
            System.out.println(pre.cal);
            pre = pre.next;
        }
    }

    public ListNode merge(ListNode l1,ListNode l2) {
        if (l1 == null) {
            return l2;
        }
        if (l2 == null) {
            return l1;
        }

        if (l1.cal <= l2.cal) {
            l1.next = merge(l1.next,l2);
            return l1;
        } else {
            l2.next = merge(l1,l2.next);
            return l2;
        }
    }


}


