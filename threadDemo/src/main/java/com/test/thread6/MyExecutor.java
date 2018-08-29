package com.test.thread6;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/29
 * @modifyDate: 11:24
 * @Description:
 */
public class MyExecutor extends ThreadPoolExecutor {

    private ConcurrentHashMap<String,Date> startTimes;

    public MyExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.startTimes = new ConcurrentHashMap<>();
    }

    @Override
    public void shutdown() {
        System.out.printf("MyExecutor : Going to shutDown .\n");
        System.out.printf("MyExecutor : executed tasks %d .\n",getCompletedTaskCount());
        System.out.printf("MyExecutor : running tasks %d .\n",getActiveCount());
        System.out.printf("MyExecutor : pending tasks %d .\n",getQueue().size());
        super.shutdown();
    }

    @Override
    public List<Runnable> shutdownNow() {
        System.out.printf("MyExecutor : going to immediately shutdown.\n ");
        System.out.printf("MyExecutor : executed tasks: %d \n ",getCompletedTaskCount());
        System.out.printf("MyExecutor : running tasks: %d \n ",getActiveCount());
        System.out.printf("MyExecutor : pending tasks: %d \n ",getQueue().size());
        return super.shutdownNow();
    }

    @Override
    protected void beforeExecute(Thread t, Runnable r) {
        System.out.printf("MyExecutor : a task is beginning: %s :%s\n ",t.getName(),r.hashCode());
        startTimes.put(String.valueOf(r.hashCode()),new Date());
    }

    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        Future<?> result = (Future<?>) r;
        try {
            System.out.println("*********************************");
            System.out.printf("MyExecutor : a task is finishing .\n");
            System.out.printf("MyExecutor : result : %s \n",result.get());
            Date startDate = startTimes.remove(String.valueOf(r.hashCode()));
            Date finishDate = new Date();
            long diff = finishDate.getTime() - startDate.getTime();
            System.out.printf("MyExecutor  : Duration %d \n",diff);
            System.out.println("*********************************");
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        MyExecutor myExecutor = new MyExecutor(2,4,1000,TimeUnit.MILLISECONDS,
                new LinkedBlockingDeque<Runnable>());
        List<Future<String>> results = new ArrayList<>();
        for (int i=0;i<10;i++) {
            SleepTwoSecondTask task = new SleepTwoSecondTask();
            Future<String> future = myExecutor.submit(task);
            results.add(future);
        }

        for (int i=0;i<5;i++) {
            try {
                String result = results.get(i).get();
                System.out.printf("Main : result for task %d : %s \n",i,result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        myExecutor.shutdown();

        for (int i=5;i<10;i++) {
            try {
                String result = results.get(i).get();
                System.out.printf("Main: result for task %d : %s \n",i,result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        try {
            myExecutor.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Main :end of program.");
    }
}
class SleepTwoSecondTask implements Callable<String> {

    @Override
    public String call() throws Exception {
        TimeUnit.SECONDS.sleep(2);
        return new Date().toString();
    }
}
