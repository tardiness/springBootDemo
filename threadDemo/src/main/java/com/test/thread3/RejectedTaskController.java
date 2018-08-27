package com.test.thread3;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RejectedTaskController implements RejectedExecutionHandler {


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.printf("RejectedTaskController: the task %s has been rejected\n",r.toString());
        System.out.printf("RejectedTaskController: %s\n",executor.toString());
        System.out.printf("RejectedTaskController: terminating : %s \n",executor.isTerminating());
        System.out.printf("RejectedTaskController: terminated : %s \n",executor.isTerminated());
    }

    public static void main(String[] args) {
        RejectedTaskController rejectedTaskController = new RejectedTaskController();
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        executor.setRejectedExecutionHandler(rejectedTaskController);
        System.out.printf("Main :Starting \n");
        for (int i=0;i<3;i++) {
            TaskRejected rejected = new TaskRejected("task" + i);
            executor.submit(rejected);
        }
        System.out.printf("Main: shutting down the executor\n");
        executor.shutdown();
        System.out.printf("Main : send another task\n");
        TaskRejected rejected = new TaskRejected("rejected");
        executor.submit(rejected);
        System.out.println("main: end");
    }
}
class TaskRejected implements Runnable {

    private String name;

    public TaskRejected(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.printf("Task:"+name+" starting\n");
        try {
            long duration = (long) (Math.random() * 10);
            System.out.printf("Task_%s:ReportGenerator:generating a report duration %s seconds\n",name,duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Task %s :end",name);
    }

    @Override
    public String toString() {
        return "TaskRejected{" +
                "name='" + name + '\'' +
                '}';
    }
}
