package com.test.thread6;

import java.util.Date;
import java.util.concurrent.*;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/1
 * @modifyDate: 10:21
 * @Description:
 */
public class MyScheduledTask<V> extends FutureTask<V> implements RunnableScheduledFuture<V> {

    private RunnableScheduledFuture<V> task;
    private ScheduledThreadPoolExecutor executor;
    private long period;
    private long startDate;

    public MyScheduledTask(Runnable runnable, V result, RunnableScheduledFuture<V> task, ScheduledThreadPoolExecutor executor) {
        super(runnable, result);
        this.task = task;
        this.executor = executor;
    }

    public static void main(String[] args) {
        MyScheduledThreadPoolExecutor executor = new MyScheduledThreadPoolExecutor(2);
        Task task = new Task();
        System.out.printf("Main : %s \n",new Date());
        executor.schedule(task,1,TimeUnit.SECONDS);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        task = new Task();
        System.out.printf("Main : %s \n",new Date());
        executor.scheduleAtFixedRate(task,1,3,TimeUnit.SECONDS);

        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executor.shutdown();

        try {
            executor.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main : end of program\n");
    }

    @Override
    public boolean isPeriodic() {
        return task.isPeriodic();
    }

    @Override
    public long getDelay(TimeUnit unit) {
        if (!isPeriodic()) {
            return task.getDelay(unit);
        } else {
            if (startDate == 0) {
                return task.getDelay(unit);
            } else {
                Date now = new Date();
                long delay = startDate - now.getTime();
                return unit.convert(delay,TimeUnit.MILLISECONDS);
            }
        }
    }

    @Override
    public int compareTo(Delayed o) {
        return task.compareTo(o);
    }

    @Override
    public void run() {
        if (isPeriodic() && (!executor.isShutdown())) {
            Date now = new Date();
            startDate = now.getTime() + period;
            executor.getQueue().add(this);
        }

        System.out.printf("Pre-MyScheduledTask : %s \n",new Date());
        System.out.printf("MyScheduledTask: is periodic %s \n",isPeriodic());
        super.runAndReset();
        System.out.printf("Post-MyScheduledTask: %s \n",new Date());
    }

    public void setPeriod(long period) {
        this.period = period;
    }
}

class MyScheduledThreadPoolExecutor extends ScheduledThreadPoolExecutor {

    public MyScheduledThreadPoolExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    @Override
    protected <V> RunnableScheduledFuture<V> decorateTask(Runnable runnable, RunnableScheduledFuture<V> task) {
        MyScheduledTask<V> myTask = new MyScheduledTask<>(runnable,null,task,this);
        return myTask;
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        ScheduledFuture<?> future = super.scheduleAtFixedRate(command, initialDelay, period, unit);
        MyScheduledTask<?> task = (MyScheduledTask<?>) future;
        task.setPeriod(TimeUnit.MILLISECONDS.convert(period,unit));
        //返回的是原来的 future
        return future;
    }
}

class Task implements Runnable {

    @Override
    public void run() {
        System.out.printf("Task : begin \n");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Task : end \n");
    }
}