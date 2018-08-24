package com.test.thread3;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/23
 * @modifyDate: 10:29
 * @Description:
 */
public class Task implements Runnable {

    private Date initData;
    private String name;

    public Task( String name) {
        this.initData = new Date();
        this.name = name;
    }

    public static void main(String[] args) {
        Server server = new Server();
        for (int i=0;i<100;i++) {
            Task task = new Task("Task" + i);
            server.executeTask(task);
        }
        server.endServer();
    }

    @Override
    public void run() {
        System.out.printf("%s: task created on: %s \n",Thread.currentThread().getName(),initData);
        System.out.printf("%s: task %s: Started on: %s \n",Thread.currentThread().getName(),name,new Date());
        try {
            long duration = (long) (Math.random()*10);
            System.out.printf("%s : task %s :doing something duration %s seconds \n",Thread.currentThread().getName(),name,duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s: task %s:finished on : %s \n",Thread.currentThread().getName(),name,new Date());
    }
}
class Server {

    private ThreadPoolExecutor executor;

    public Server() {
        this.executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
    }

    public void executeTask(Task task) {
        System.out.printf("Server :a new task has arrived\n");
        executor.execute(task);
        System.out.printf("Server : Pool Size :%d\n",executor.getPoolSize());
        System.out.printf("Server : Active count :%d\n",executor.getActiveCount());
        System.out.printf("Server : complete task :%d\n",executor.getCompletedTaskCount());
        System.out.printf("Server : Task Count :%d\n",executor.getTaskCount());

    }

    public void endServer() {
        executor.shutdown();
    }

}
