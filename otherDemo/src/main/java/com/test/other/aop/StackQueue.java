package com.test.other.aop;

import java.util.Stack;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2019/3/22
 * @modifyDate: 16:22
 * @Description:
 */
public class StackQueue {

    Stack<Integer> stack1 = new Stack<>();
    Stack<Integer> stack2 = new Stack<>();


    public void push(int node) {
        stack1.push(node);
    }

    public int pop() {
        if (stack1.empty() && stack2.empty()) {
            throw new RuntimeException("queue is empty");
        }
        if (stack2.empty()) {
            while (!stack1.empty()) {
                stack2.push(stack1.pop());
            }
        }
        return stack2.pop();
    }
}
