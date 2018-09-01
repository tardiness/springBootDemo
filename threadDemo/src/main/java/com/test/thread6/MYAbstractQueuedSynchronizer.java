package com.test.thread6;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/1
 * @modifyDate: 17:18
 * @Description:
 */
public class MYAbstractQueuedSynchronizer extends AbstractQueuedSynchronizer {

    private AtomicInteger state;

    public MYAbstractQueuedSynchronizer() {
        state = new AtomicInteger(0);
    }

    @Override
    protected boolean tryAcquire(int arg) {
        return state.compareAndSet(0,1);
    }

    @Override
    protected boolean tryRelease(int arg) {
        return state.compareAndSet(1,0);
    }

    public static void main(String[] args) {
        MyLock lock = new MyLock();

        for (int i=0;i<10;i++) {
            LockTask task = new LockTask(lock,"task"+i);
            Thread thread = new Thread(task);
            thread.start();
        }

        Boolean value;
        do {
            try {
                value = lock.tryLock(1,TimeUnit.SECONDS);
                if (!value) {
                    System.out.printf("Main : trying to get the lock\n ");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                value = false;
            }
        } while (!value);

        System.out.println("Main : got the lock \n");
        lock.unlock();
        System.out.printf("Main : end of program \n");
    }

}

class MyLock implements Lock {

    private AbstractQueuedSynchronizer synchronizer;

    public MyLock() {
        synchronizer = new MYAbstractQueuedSynchronizer();
    }

    @Override
    public void lock() {
        synchronizer.acquire(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        synchronizer.acquireInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        try {
            return synchronizer.tryAcquireSharedNanos(1,1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return synchronizer.tryAcquireNanos(1,TimeUnit.MILLISECONDS.convert(time,unit));
    }

    @Override
    public void unlock() {
        synchronizer.release(1);
    }

    @Override
    public Condition newCondition() {
        return synchronizer.new ConditionObject();
    }
}

class LockTask implements Runnable {

    private MyLock lock;
    private String name;

    public LockTask(MyLock lock, String name) {
        this.lock = lock;
        this.name = name;
    }

    @Override
    public void run() {
        lock.lock();
        System.out.printf("Task : %s : take lock\n",name);

        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.printf("task : %s free the lock \n",name);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}
