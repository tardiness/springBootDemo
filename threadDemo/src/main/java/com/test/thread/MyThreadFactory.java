package com.test.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/8
 * @modifyDate: 17:54
 * @Description:
 */
public class MyThreadFactory implements ThreadFactory {

    private int counter;
    private String name;
    private List<String> stats;

    public MyThreadFactory(String name) {
        this.counter = 0;
        this.name = name;
        this.stats = new ArrayList<>();
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r,name+"-Thread_"+counter);
        counter++;
        stats.add(String.format("Created thread %d with name %s on %s\n",t.getId(),t.getName(),new Date()));
        return t;
    }

    public String getStats() {
        StringBuffer buffer = new StringBuffer();
        Iterator<String> iterator = stats.iterator();
        while (iterator.hasNext()) {
            buffer.append(iterator.next());
            buffer.append("\n");
        }
        return buffer.toString();
    }

    public static void main(String[] args) {
        MyThreadFactory factory = new MyThreadFactory("MyThreadFactory");
        FactoryTask factoryTask = new FactoryTask();
        Thread thread;

        for (int i=0;i<10;i++) {
            thread = factory.newThread(factoryTask);
            thread.start();
        }

        System.out.printf("Factory stats:\n");
        System.out.printf("%s\n",factory.getStats());
    }

}

class FactoryTask implements Runnable {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


