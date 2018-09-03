package com.test.thread7;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/3
 * @modifyDate: 10:25
 * @Description:
 */
public class ExecutorTask implements Runnable {

    private long milliseconds;

    public ExecutorTask(long milliseconds) {
        this.milliseconds = milliseconds;
    }

    @Override
    public void run() {
        System.out.printf("%s : begin \n",Thread.currentThread().getName());
        try {
            TimeUnit.MILLISECONDS.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s : end \n",Thread.currentThread().getName());
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        Random random = new Random();
        for (int i=0;i<10;i++) {
            ExecutorTask task = new ExecutorTask(random.nextInt(10000));
            executor.submit(task);
        }

        for (int i=0;i<5;i++) {
            showLog(executor);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        for (int i=0;i<5;i++) {
            showLog(executor);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            executor.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main : end of program. \n");
    }

    private static void showLog(ThreadPoolExecutor executor) {
        System.out.println("************************************");
        System.out.printf("Main : executor log");
        System.out.printf("Main : executor : core pool size : %d \n",executor.getCorePoolSize());
        System.out.printf("Main : executor : active count : %d \n",executor.getActiveCount());
        System.out.printf("Main : executor : task count : %d \n",executor.getTaskCount());
        System.out.printf("Main : executor : complete task count : %d \n",executor.getCompletedTaskCount());
        System.out.printf("Main : executor : shutdown :%s \n",executor.isShutdown());
        System.out.printf("Main : executor : terminating :%s \n",executor.isTerminating());
        System.out.printf("Main : executor : terminated :%s \n",executor.isTerminated());

        System.out.println("************************************");
    }
}
