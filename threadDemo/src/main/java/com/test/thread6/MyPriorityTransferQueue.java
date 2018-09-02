package com.test.thread6;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TransferQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class MyPriorityTransferQueue<E> extends PriorityBlockingQueue<E> implements TransferQueue<E> {

    private AtomicInteger counter;
    private LinkedBlockingDeque<E> transfer;
    private ReentrantLock lock;

    public MyPriorityTransferQueue() {

        counter = new AtomicInteger(0);
        lock = new ReentrantLock();
        transfer = new LinkedBlockingDeque<>();
    }

    @Override
    public boolean tryTransfer(E e) {
        lock.lock();
        boolean value;
        if (counter.get() == 0) {
            value = false;
        } else {
            put(e);
            value = true;
        }
        lock.unlock();
        return value;
    }

    @Override
    public void transfer(E e) throws InterruptedException {
        lock.lock();
        if (counter.get() != 0) {
            put(e);
            lock.unlock();
        } else {
            transfer.add(e);
            lock.unlock();
            synchronized (e) {
                e.wait();
            }
        }
    }

    @Override
    public boolean tryTransfer(E e, long timeout, TimeUnit unit) throws InterruptedException {
        lock.lock();
        if (counter.get() != 0) {
            put(e);
            lock.unlock();
            return true;
        } else {
            transfer.add(e);
            long newTimeOut = TimeUnit.MILLISECONDS.convert(timeout,unit);
            lock.unlock();
            e.wait(newTimeOut);
            lock.lock();
            if (transfer.contains(e)) {
                transfer.remove(e);
                lock.unlock();
                return false;
            } else {
                lock.unlock();
                return true;
            }
        }
    }

    @Override
    public boolean hasWaitingConsumer() {
        return (counter.get()!=0);
    }

    @Override
    public int getWaitingConsumerCount() {
        return counter.get();
    }

    @Override
    public E take() throws InterruptedException {
        lock.lock();
        counter.incrementAndGet();
        E value = transfer.poll();
        if (value == null) {
            lock.unlock();
            value = super.take();
            lock.lock();
        } else {
            synchronized (value) {
                value.notify();
            }
        }
        counter.decrementAndGet();
        lock.unlock();
        return value;
    }


    public static void main(String[] args) {
        MyPriorityTransferQueue<Event> buffer = new MyPriorityTransferQueue<>();
        Producer producer = new Producer(buffer);
        Thread[] threads = new Thread[10];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new Thread(producer);
            threads[i].start();
        }

        Consumer consumer = new Consumer(buffer);
        Thread thread = new Thread(consumer);
        thread.start();

        System.out.printf("Main: buffer : Consumer counter %d \n",buffer.getWaitingConsumerCount());

        Event event = new Event("Core Event",0);
        try {
            buffer.transfer(event);
            System.out.printf("Main : my event has been transfered .\n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i=0;i<threads.length;i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main : buffer :consumer count : %d \n",buffer.getWaitingConsumerCount());

        event = new Event("Core event 2",0);
        try {
            buffer.transfer(event);
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main : end of program");

    }
}

class Event implements Comparable<Event> {

    private String thread;
    private int priority;

    public Event(String thread, int priority) {
        this.thread = thread;
        this.priority = priority;
    }

    public String getThread() {
        return thread;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Event o) {
        if (this.getPriority() > o.getPriority()) {
            return -1;
        } else if (this.getPriority() < o.getPriority()) {
            return 1;
        } else {
            return 0;
        }
    }
}

class Producer implements Runnable {

    private MyPriorityTransferQueue<Event> buffer;

    public Producer(MyPriorityTransferQueue<Event> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i =0;i<100;i++) {
            Event event = new Event(Thread.currentThread().getName(),i);
            buffer.put(event);
        }
    }
}

class Consumer implements Runnable {

    private MyPriorityTransferQueue<Event> buffer;

    public Consumer(MyPriorityTransferQueue<Event> buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        for (int i=0;i<1002;i++) {
            try {
                Event value = buffer.take();
                System.out.printf("Consumer : %s : %d \n",value.getThread(),value.getPriority());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
     }
}
