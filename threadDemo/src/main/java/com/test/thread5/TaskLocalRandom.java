package com.test.thread5;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/29
 * @modifyDate: 10:30
 * @Description:
 */
public class TaskLocalRandom implements Runnable {

    public TaskLocalRandom() {
        ThreadLocalRandom.current();
    }

    @Override
    public void run() {
        String name = Thread.currentThread().getName();
        for (int i=0;i<10;i++) {
            System.out.printf("%s : %d \n",name,ThreadLocalRandom.current().nextInt(10));
        }
    }

    public static void main(String[] args) {
        Thread[] threads = new Thread[3];

        for (int i = 0;i<threads.length;i++) {
            TaskLocalRandom random = new TaskLocalRandom();
            threads[i] = new Thread(random);
            threads[i].start();
        }
    }
}
