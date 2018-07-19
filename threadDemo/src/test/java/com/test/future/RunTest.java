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

//        new RunTest().multiplicative();
    }

    public void multiplicative() {
        for (int i = 0; i <= 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j=0;j<=10;j++) {
                        for (int k = 0;k<=10;k++) {
                            System.out.printf("%s: %d * %d = %d\n",Thread.currentThread().getName(),j,k,j*k);
                        }
                    }
                }
            }).start();
        }
    }
}
