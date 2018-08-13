package com.test.thread1;

import java.util.LinkedList;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/13
 * @modifyDate: 9:08
 * @Description:
 */
public class FileMock {

    private String[] content;

    private int index;

    public static void main(String[] args) {
        FileMock mock = new FileMock(100,10);
        Buffer buffer = new Buffer(20);
        Producer1 producer1 = new Producer1(mock,buffer);
        Thread thread = new Thread(producer1,"producer1");

        Consumer1[] consumer1s = new Consumer1[3];
        Thread[] threads = new Thread[3];
        for (int i=0;i<3;i++) {
            consumer1s[i] = new Consumer1(buffer);
            threads[i] = new Thread(consumer1s[i],"consumer1s" + i);
        }

        thread.start();
        for (int i=0;i<3;i++) {
            threads[i].start();
        }
    }

    public FileMock(int size,int length) {
        content = new String[size];
        for (int i=0;i<size;i++) {
            StringBuffer buffer = new StringBuffer(length);
            for (int j=0;j<length;j++) {
                int indice = (int) Math.random()*255;
                buffer.append((char) indice);
            }
            content[i] = buffer.toString();
        }
    }

    public boolean hasMoreLine() {
        return index < content.length;
    }

    public String getLine() {
        if (this.hasMoreLine()) {
            System.out.println("Mock: " + (content.length - index));
            return content[index++];
        }
        return null;
    }
}

class Buffer {

    private LinkedList<String> buffer;
    private int maxSize;
    private ReentrantLock lock;
    private Condition lines;
    private Condition space;
    private boolean pendingLines;

    public Buffer(int maxSize) {
        this.maxSize = maxSize;
        buffer = new LinkedList<>();
        lock = new ReentrantLock();
        lines = lock.newCondition();
        space = lock.newCondition();
        pendingLines = true;
    }

    public void insert(String line) {
        lock.lock();
        try {
            while (buffer.size() == maxSize) {
                space.await();
            }
            buffer.offer(line);
            System.out.printf("%s:Inserted Line : %d\n",Thread.currentThread().getName(),buffer.size());
            lines.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public String get() {
        String line = null;
        lock.lock();
        try {
            while ((buffer.size()==0) && (isPendingLines())) {
                lines.await();
            }
            if (isPendingLines()) {
                line = buffer.poll();
                System.out.printf("%s :Line Readed :%d \n",Thread.currentThread().getName(),buffer.size());
                space.signalAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return line;
    }

    public boolean isPendingLines() {
        return pendingLines || buffer.size() > 0;
    }

    public void setPendingLines(boolean pendingLines) {
        this.pendingLines = pendingLines;
    }
}

class Producer1 implements Runnable {

    private FileMock mock;
    private Buffer buffer;

    public Producer1(FileMock mock, Buffer buffer) {
        this.mock = mock;
        this.buffer = buffer;
    }

    @Override
    public void run() {
        buffer.setPendingLines(true);
        while (mock.hasMoreLine()) {
            String line = mock.getLine();
            buffer.insert(line);
        }
        buffer.setPendingLines(false);
    }
}

class Consumer1 implements Runnable {

    private Buffer buffer;

    public Consumer1(Buffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void run() {
        while (buffer.isPendingLines()) {
            String line = buffer.get();
            processLine(line);
        }
    }

    private void processLine(String line) {
        try {
            Random random = new Random();
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
