package com.test.thread4;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/28
 * @modifyDate: 15:14
 * @Description:
 */
public class ExceptionTask extends RecursiveTask<Integer> {

    private int[] array;
    private int start,end;

    public ExceptionTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected Integer compute() {
        System.out.printf("Task : start from %d to %d \n",start,end);
        if (end-start < 10) {
            if (3>start && 3<end) {
//                throw new RuntimeException("this task throw an exception : task from "+start+" to "+ end);
                Exception e = new RuntimeException("this task throw an exception : task from "+start+" to "+ end);
                completeExceptionally(e);
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            int mid = (start+end)/2;
            ExceptionTask task = new ExceptionTask(array,start,mid);
            ExceptionTask task1 = new ExceptionTask(array,mid,end);
            invokeAll(task,task1);
        }
        System.out.printf("Task : end from %d to %d \n",start,end);
        return 0;
    }

    public static void main(String[] args) {
        int[] array = new int[100];
        ExceptionTask task = new ExceptionTask(array,0,100);
        ForkJoinPool pool = new ForkJoinPool();
        pool.execute(task);
        pool.shutdown();
        try {
            pool.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (task.isCompletedAbnormally()) {
            System.out.printf("Main: an exception has occurred \n");
            System.out.printf("Main: %s \n",task.getException());
        }
        System.out.printf("Main: result : %d",task.join());
    }
}
