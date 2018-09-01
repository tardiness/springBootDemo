package com.test.thread6;

import com.test.thread3.Task;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/1
 * @modifyDate: 9:48
 * @Description:
 */
public class MyThread extends Thread {

    private Date creationDate;
    private Date startDate;
    private Date finishDate;

    public MyThread(Runnable target, String name) {
        super(target, name);
        setCreationDate();
    }

    @Override
    public void run() {
        setStartDate();
        super.run();
        setFinishDate();
    }

    public void setCreationDate() {
        this.creationDate = new Date();
    }

    public void setStartDate() {
        this.startDate = new Date();
    }

    public void setFinishDate() {
        this.finishDate = new Date();
    }

    public Long getExecutionTime() {
        return this.finishDate.getTime() - this.startDate.getTime();
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getName());
        builder.append(": ");
        builder.append("creationDate : ");
        builder.append(creationDate);
        builder.append(": running time :");
        builder.append(getExecutionTime());
        builder.append(" milliseconds.");
        return builder.toString();
    }



    public static void main(String[] args) {
        MyThreadFactory myThreadFactory = new MyThreadFactory("MyThreadFactory");
        MyTask myTask = new MyTask();
        Thread thread = myThreadFactory.newThread(myTask);
        Thread thread1 = myThreadFactory.newThread(myTask);

        thread.start();
        thread1.start();
        try {
            thread.join();
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main : thread information.\n");
        System.out.printf("%s \n",thread);
        System.out.printf("%s \n",thread1);
        System.out.printf("Main : end of example. \n");

        MyThreadFactory1();
    }

    public static void MyThreadFactory1 () {
        MyThreadFactory myThreadFactory = new MyThreadFactory("MyThreadFactory1");
        ExecutorService executorService = Executors.newCachedThreadPool(myThreadFactory);
        MyTask myTask = new MyTask();
        executorService.submit(myTask);
        executorService.shutdown();

        try {
            executorService.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Main : end of MyThreadFactory1");

    }
}
class MyThreadFactory implements ThreadFactory {

    private int counter;
    private String prefix;

    public MyThreadFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        MyThread myThread = new MyThread(r,prefix+"-"+counter);
        counter++;
        return myThread;
    }
}

class MyTask implements Runnable {

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
