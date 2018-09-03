package com.test.thread7;

import com.mysql.jdbc.TimeUtil;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/3
 * @modifyDate: 10:46
 * @Description:
 */
public class FJTask extends RecursiveAction {

    private int[] array;
    private int start,end;

    public FJTask(int[] array, int start, int end) {
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end-start>100) {
            int mid = (start+end)/2;
            FJTask task = new FJTask(array,start,mid);
            FJTask task1 = new FJTask(array,mid,end);

            task.fork();
            task1.fork();

            task.join();
            task1.join();
        } else {
            for (int i=start;i<end;i++) {
                array[i]++;
                try {
                    Thread.sleep(5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        int[] array = new int[10000];
        FJTask task = new FJTask(array,0,array.length);
        pool.execute(task);
        while (!task.isDone()) {
            showLog(pool);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        pool.shutdown();
        try {
            pool.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        showLog(pool);
        System.out.println("Main : end of program");
    }

    private static void showLog(ForkJoinPool pool) {
        System.out.println("******************************");
        System.out.printf("Main : forkJoin pool log \n");
        System.out.printf("Main : forkJoin pool :parallelism : %d  \n",pool.getParallelism());
        System.out.printf("Main : forkJoin pool :poolSize : %d  \n",pool.getPoolSize());
        System.out.printf("Main : forkJoin pool :active count : %d  \n",pool.getActiveThreadCount());
        System.out.printf("Main : forkJoin pool :running count : %d  \n",pool.getRunningThreadCount());
        System.out.printf("Main : forkJoin pool :queued submission : %d  \n",pool.getQueuedSubmissionCount());
        System.out.printf("Main : forkJoin pool :queued task : %d  \n",pool.getQueuedTaskCount());
        System.out.printf("Main : forkJoin pool :queued submissions : %s  \n",pool.hasQueuedSubmissions());
        System.out.printf("Main : forkJoin pool :steal count  : %d  \n",pool.getStealCount());
        System.out.printf("Main : forkJoin pool :isTerminated  : %s  \n",pool.isTerminated());
        System.out.println("******************************");
    }
}
