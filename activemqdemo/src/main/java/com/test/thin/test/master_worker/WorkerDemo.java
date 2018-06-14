package com.test.thin.test.master_worker;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkerDemo implements Runnable{

    private ConcurrentLinkedQueue<TaskDemo> workQueue;
    private ConcurrentHashMap<String, Object> resultMap;

    @Override
    public void run() {
        while (true) {
            TaskDemo input = this.workQueue.poll();
            // 所有任务已经执行完毕
            if (input == null) {
                break;
            }
            // 模拟对task进行处理, 返回结果
            int result = input.getPrice();
            this.resultMap.put(input.getId() + "", result);
            System.out.println("任务执行完毕, 当前线程: " + Thread.currentThread().getName());
        }
    }

    public ConcurrentLinkedQueue<TaskDemo> getWorkQueue() {
        return workQueue;
    }

    public void setWorkQueue(ConcurrentLinkedQueue<TaskDemo> workQueue) {
        this.workQueue = workQueue;
    }

    public ConcurrentHashMap<String, Object> getResultMap() {
        return resultMap;
    }

    public void setResultMap(ConcurrentHashMap<String, Object> resultMap) {
        this.resultMap = resultMap;
    }
}