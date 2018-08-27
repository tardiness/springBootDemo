package com.test.thread3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class Result {

    private String name;
    private int value;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Task1> task1s = new ArrayList<>();
        for (int i=0;i<3;i++) {
            Task1 task1 = new Task1(i+"");
            task1s.add(task1);
        }
        List<Future<Result>> futures = null;
        try {
            futures = executorService.invokeAll(task1s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        System.out.println("Printing results");
        for (int i=0;i<futures.size();i++) {
            Future<Result> future = futures.get(i);
            try {
                Result result = future.get();
                System.out.println(result.getName()+":" + result.getValue());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}

class Task1 implements Callable<Result> {

    private String name;

    public Task1(String name) {
        this.name = name;
    }

    @Override
    public Result call() throws Exception {
        System.out.printf("%s :Starting \n",this.name);
        long duration = (long) (Math.random() * 10);
        System.out.printf("%s :Waiting %s seconds for results \n",Thread.currentThread().getName(),duration);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int value = 0;
        for (int i=0;i<5;i++) {
            value += (Math.random()*100);
        }
        Result result = new Result();
        result.setName(this.name);
        result.setValue(value);
        System.out.println(this.name + ": ends");

        return result;
    }
}
