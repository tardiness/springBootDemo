package com.test.thread1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/10
 * @modifyDate: 11:51
 * @Description:
 */
public class PrintQueue {

    private final Lock queueLock = new ReentrantLock(false);

    public void printJob(Object document) {
        queueLock.lock();
        try {
            Long duration = (long)(Math.random()*10000);
            System.out.printf(Thread.currentThread().getName()+"PrintQueue:printing a job during "+ (duration/1000)+"seconds\n");
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            queueLock.unlock();
        }

        queueLock.lock();
        try {
            Long duration = (long)(Math.random()*10000);
            System.out.printf(Thread.currentThread().getName()+"PrintQueue:printing a job during "+ (duration/1000)+"seconds \n");
            Thread.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            queueLock.unlock();
        }

    }

    public static void main(String[] args) {
        PrintQueue printQueue = new PrintQueue();
        Thread[] threads = new Thread[10];
        for (int i=0;i<10;i++) {
            threads[i] = new Thread(new Job(printQueue),"Thread" + i);
        }

        for (int i=0;i<10;i++) {
            threads[i].start();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Job implements Runnable {

    private PrintQueue printQueue;

    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.printf("%s:Going to print a document \n",Thread.currentThread().getName());
        printQueue.printJob(new Object());
        System.out.printf("%s: the document has been printed \n",Thread.currentThread().getName());
    }
}
