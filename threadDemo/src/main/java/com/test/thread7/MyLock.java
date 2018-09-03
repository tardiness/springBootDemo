package com.test.thread7;

import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/3
 * @modifyDate: 9:39
 * @Description:
 */
public class MyLock extends ReentrantLock {

    public String getOwnerName() {
        if (this.getOwner() == null) {
            return "None";
        } else {
            return this.getOwner().getName();
        }
    }

    public Collection<Thread> getThreads() {
        return this.getQueuedThreads();
    }

    public static void main(String[] args) {
        MyLock myLock = new MyLock();
        Thread[] threads = new Thread[5];
        for (int i=0;i<threads.length;i++) {
            LockTask task = new LockTask(myLock);
            threads[i] = new Thread(task);
            threads[i].start();
        }
        for (int i=0;i<15;i++) {
            System.out.printf("Main:logging the lock\n");
            System.out.println("********************************");
            System.out.printf("Lock : owner : %s \n ",myLock.getOwnerName());
            System.out.printf("Lock : Queued threads : %s \n",myLock.hasQueuedThreads());
            if (myLock.hasQueuedThreads()) {
                System.out.printf("lock: queued length : %d \n",myLock.getQueueLength());
                System.out.printf("lock: queued threads : \n ");
                Collection<Thread> lockedThreads = myLock.getThreads();
                for (Thread thread : lockedThreads) {
                    System.out.printf("%s \n",thread.getName());
                }
                System.out.println("");
            }
            System.out.printf("lock : fairness : %s \n",myLock.isFair());
            System.out.printf("lock : locked : %s \n",myLock.isLocked());
            System.out.println("********************************");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}

class LockTask implements Runnable {

    private Lock lock;

    public LockTask(Lock lock) {
        this.lock = lock;
    }

    @Override
    public void run() {
        for (int i=0;i<5;i++) {
            lock.lock();
            System.out.printf("%s : get the lock.\n",Thread.currentThread().getName());
            try {
                TimeUnit.MILLISECONDS.sleep(500);
                System.out.printf("%s : free the lock.\n",Thread.currentThread().getName());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
