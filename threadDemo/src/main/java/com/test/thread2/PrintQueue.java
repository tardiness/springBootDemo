package com.test.thread2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/13
 * @modifyDate: 11:39
 * @Description:
 */
public class PrintQueue {

    private final Semaphore semaphore;

    private Boolean freePrinters[];

    private Lock lockPrinters;
    private int printer;

    public PrintQueue() {
        this.semaphore = new Semaphore(3);
        this.freePrinters = new Boolean[3];
        for (int i=0;i<3;i++) {
            this.freePrinters[i] = true;
        }
        this.lockPrinters = new ReentrantLock();
    }

    public void printJob(Object document) {
        try {
            semaphore.acquire();
            int assignedPrinter = getPrinter();
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s :printQueue : printing a job in Printer %d during %d second\n",
                    Thread.currentThread().getName(), assignedPrinter, duration);
            TimeUnit.SECONDS.sleep(duration);
            freePrinters[assignedPrinter] = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            semaphore.release();
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
        }
    }

    public int getPrinter() {
        int ret = -1;
        try {
            lockPrinters.lock();
            for (int i=0;i<freePrinters.length;i++) {
                if (freePrinters[i]) {
                    ret = i;
                    freePrinters[i] = false;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lockPrinters.unlock();
        }
        return ret;
    }
}

class Job implements Runnable {

    private PrintQueue printQueue;

    public Job(PrintQueue printQueue) {
        this.printQueue = printQueue;
    }

    @Override
    public void run() {
        System.out.printf("%s : going to print a job \n",Thread.currentThread().getName());
        printQueue.printJob(new Object());
        System.out.printf("%s : the document has been printed \n",Thread.currentThread().getName());
    }
}
