package com.test.thread;

import java.util.Random;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/8
 * @modifyDate: 17:42
 * @Description:
 */
public class MyThreadGroup extends ThreadGroup {

    public static void main(String[] args) {
        MyThreadGroup myThreadGroup = new MyThreadGroup("MyThreadGroup");
        Task task = new Task();
        for (int i=0;i <2;i++) {
            Thread t = new Thread(myThreadGroup,task);
            t.start();
        }
    }


    public MyThreadGroup(String name) {
        super(name);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.printf("the thread %s has thrown an exception \n",t.getId());
        e.printStackTrace();
        System.out.printf("terminating the rest of the threads \n");
        interrupt();
    }
}

class Task implements Runnable {

    @Override
    public void run() {
        int result;
        Random random = new Random(Thread.currentThread().getId());
        while (true) {
            result = 1000/((int) random.nextDouble()*1000);
            System.out.printf("%s : %d\n",Thread.currentThread().getId(),result);
            if (Thread.currentThread().isInterrupted()) {
                System.out.printf("%d : interrupted \n",Thread.currentThread().getId());
            }
        }
    }
}
