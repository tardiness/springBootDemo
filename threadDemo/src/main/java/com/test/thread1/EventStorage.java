package com.test.thread1;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/10
 * @modifyDate: 10:21
 * @Description:
 */
public class EventStorage {

    private int maxSize;

    private List<Date> storage;

    public EventStorage() {
        maxSize = 10;
        storage = new LinkedList<>();
    }

    public synchronized void set() {
        while (storage.size() == maxSize) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        storage.add(new Date());
        System.out.printf("Set : %d \n",storage.size());
        notifyAll();
    }

    public synchronized void get() {
        while (storage.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Get:%d:%s \n",storage.size(),((LinkedList<?>) storage).poll());
        notifyAll();
    }

    public static void main(String[] args) {
        EventStorage eventStorage = new EventStorage();
        Consumer consumer = new Consumer(eventStorage);
        Thread thread = new Thread(consumer,"consumer");
        Producer producer = new Producer(eventStorage);
        Thread thread1 = new Thread(producer,"producer");

        thread.start();
        thread1.start();
    }
}

class Producer implements Runnable {

    private EventStorage storage;

    public Producer(EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i=0;i<100;i++) {
            storage.set();
        }
    }
}

class Consumer implements Runnable {

    private EventStorage storage;

    public Consumer(EventStorage storage) {
        this.storage = storage;
    }

    @Override
    public void run() {
        for (int i=0;i<100;i++) {
            storage.get();
        }
    }
}
