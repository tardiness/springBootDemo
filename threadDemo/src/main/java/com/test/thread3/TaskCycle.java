package com.test.thread3;


import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TaskCycle implements Runnable{

    private String name;

    public TaskCycle(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        System.out.printf("%s :starting at %s \n",name,new Date());

    }

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        System.out.printf("Main: starting at %s\n",new Date());
        TaskCycle taskCycle = new TaskCycle("task");
        ScheduledFuture<?> result = executor.scheduleAtFixedRate(taskCycle,1,2,TimeUnit.SECONDS);
        for (int i=0;i<10;i++) {
            System.out.printf("Main: Delay : %s\n",result.getDelay(TimeUnit.MILLISECONDS));
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main : finished at %s\n",new Date());
    }
}
