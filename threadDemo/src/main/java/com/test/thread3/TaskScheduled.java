package com.test.thread3;

import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class TaskScheduled implements Callable<String> {

    private String name;

    public TaskScheduled(String name) {
        this.name = name;
    }

    @Override
    public String call() throws Exception {
        System.out.printf("%s :starting at %s\n",name,new Date());
        return "hello world";
    }

    public static void main(String[] args) {
        ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(1);
        System.out.printf("Main : starting at %s\n",new Date());
        for (int i=0;i<5;i++) {
            TaskScheduled taskScheduled = new TaskScheduled("task" + i);
            executor.schedule(taskScheduled,i+1,TimeUnit.SECONDS);
        }
        executor.shutdown();
        try {
            executor.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main : ends at %s \n",new Date());
    }
}
