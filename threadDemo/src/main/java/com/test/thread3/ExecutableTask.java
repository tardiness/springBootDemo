package com.test.thread3;

import java.util.concurrent.*;

public class ExecutableTask implements Callable<String> {

    private String name;

    public String getName() {
        return name;
    }

    public ExecutableTask(String name) {
        this.name = name;
    }


    @Override
    public String call() throws Exception {
        try {
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s : waiting %s second for result\n",this.name,duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "hello world, i'm"+ name;
    }

    public static void main(String[] args) {
        ExecutorService executor = Executors.newCachedThreadPool();
        ResultTask[] resultTasks = new ResultTask[5];
        for (int i=0;i<5;i++) {
            ExecutableTask executableTask = new ExecutableTask("task"+i);
            resultTasks[i] = new ResultTask(executableTask);
            executor.submit(resultTasks[i]);
        }
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i=0;i< resultTasks.length;i++) {
            resultTasks[i].cancel(true);
        }
        for (int i=0;i<resultTasks.length;i++) {
            try {
                if (!resultTasks[i].isCancelled()) {
                    System.out.printf("%s\n",resultTasks[i].get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        executor.shutdown();
    }
}
class ResultTask extends FutureTask<String> {
    private String name;

    public ResultTask(Callable<String> callable) {
        super(callable);
        this.name = ((ExecutableTask)callable).getName();
    }

    @Override
    protected void done() {
        if (isCancelled()) {
            System.out.printf("%s : has canceled\n",name);
        } else {
            System.out.printf("%s : finished\n",name);
        }
    }
}
