package com.test.thread1;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/10
 * @modifyDate: 13:24
 * @Description:
 */
public class PriceInfo {

    private double price1;
    private double price2;

    private ReadWriteLock readWriteLock;

    public PriceInfo() {
        price1 = 1.0;
        price2 = 2.0;
        readWriteLock = new ReentrantReadWriteLock();
    }

    public double getPrice1() {
        readWriteLock.readLock().lock();
        double value = price1;
        readWriteLock.readLock().unlock();
        return value;
    }

    public double getPrice2() {
        readWriteLock.readLock().lock();
        double value = price2;
        readWriteLock.readLock().unlock();
        return value;
    }

    public void setPrice(double price1,double price2) {
        readWriteLock.writeLock().lock();
        this.price1 = price1;
        this.price2 = price2;
        readWriteLock.writeLock().unlock();
    }

    public static void main(String[] args) {
        PriceInfo priceInfo = new PriceInfo();
        Reader[] readers = new Reader[5];
        Thread[] threadReaders = new Thread[5];
        for (int i=0;i<5;i++) {
            readers[i] = new Reader(priceInfo);
            threadReaders[i] = new Thread(readers[i]);
        }

        Writer writer = new Writer(priceInfo);
        Thread threadWriter = new Thread(writer);

        for (int i=0;i<5;i++) {
            threadReaders[i].start();
        }
        threadWriter.start();
    }
}

class Reader implements Runnable {

    private PriceInfo priceInfo;

    public Reader(PriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    @Override
    public void run() {
        for (int i=0;i<10;i++) {
            System.out.printf("%s:Price 1 :%f \n",Thread.currentThread().getName(),priceInfo.getPrice1());
            System.out.printf("%s:Price 2 :%f \n",Thread.currentThread().getName(),priceInfo.getPrice2());
        }
    }
}

class Writer implements Runnable {

    private PriceInfo priceInfo;

    public Writer(PriceInfo priceInfo) {
        this.priceInfo = priceInfo;
    }

    @Override
    public void run() {
        for (int i=0;i<3;i++) {
            System.out.printf("Writer :Attempt to modify the prices.\n");
            priceInfo.setPrice(Math.random()*10,Math.random()*8);
            System.out.printf("Writer :Prices have been modified .\n");
        }
        try {
            Thread.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
