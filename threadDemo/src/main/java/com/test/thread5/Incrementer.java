package com.test.thread5;

import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/29
 * @modifyDate: 11:08
 * @Description:
 */
public class Incrementer implements Runnable {

    private AtomicIntegerArray vector;

    public Incrementer(AtomicIntegerArray vector) {
        this.vector = vector;
    }

    @Override
    public void run() {
        for (int i=0;i<vector.length();i++) {
            vector.getAndIncrement(i);
        }
    }

    public static void main(String[] args) {
        final int THREADS = 100;
        AtomicIntegerArray vector = new AtomicIntegerArray(1000);
        Incrementer incrementer = new Incrementer(vector);
        Decrementer decrementer = new Decrementer(vector);

        Thread[] inThreads = new Thread[THREADS];
        Thread[] deThreads = new Thread[THREADS];

        for (int i=0;i<THREADS;i++) {
            inThreads[i] = new Thread(incrementer);
            deThreads[i] = new Thread(decrementer);

            inThreads[i].start();
            deThreads[i].start();
        }

        for (int i=0;i<THREADS;i++) {
            try {
                inThreads[i].join();
                deThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        for (int i=0;i<vector.length();i++) {
            if (vector.get(i) != 0) {
                System.out.printf("Vector : [ %d ] : %d \n",i,vector.get(i));
            }
        }

        System.out.println("Main: end of the example");
    }
}

class Decrementer implements Runnable {

    private AtomicIntegerArray vector;

    public Decrementer(AtomicIntegerArray vector) {
        this.vector = vector;
    }

    @Override
    public void run() {
        for (int i=0;i<vector.length();i++) {
            vector.getAndDecrement(i);
        }
    }
}
