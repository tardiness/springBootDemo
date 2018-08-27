package com.test.thread3;

import java.util.concurrent.*;

public class TaskCancel implements Callable<String> {

    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        TaskCancel taskCancel = new TaskCancel();
        System.out.printf("Main : executing the task \n");
        Future<String> future = executor.submit(taskCancel);
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main: cancel the task");
        future.cancel(true);
        System.out.printf("Main : canceled : %s \n",future.isCancelled());
        System.out.printf("Main : done: %s\n",future.isDone());
        executor.shutdown();
        System.out.println("The executor has finished");
    }

    @Override
    public String call() throws Exception {
        while (true) {
            System.out.printf("Task :test \n");
            Thread.sleep(100);
        }
    }
}
