package com.test.future;

import com.test.thread.MyThread;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/7/12
 * @modifyDate: 17:45
 * @Description:
 */
public class RunTest {

    public static void main(String[] args) {
        for (int i = 0; i<=10; i++) {
            new MyThread().start();
        }
    }
}
