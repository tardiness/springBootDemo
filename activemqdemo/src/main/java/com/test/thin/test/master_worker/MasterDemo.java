package com.test.thin.test.master_worker;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MasterDemo {
    // 盛装任务的集合
    private ConcurrentLinkedQueue<TaskDemo> workQueue = new ConcurrentLinkedQueue<TaskDemo>();
    // 所有worker
    private HashMap<String, Thread> workers = new HashMap<>();
    // 每一个worker并行执行任务的结果
    private ConcurrentHashMap<String, Object> resultMap = new ConcurrentHashMap<>();

    public MasterDemo(WorkerDemo worker, int workerCount) {
        // 每个worker对象都需要持有queue的引用, 用于领任务与提交结果
        worker.setResultMap(resultMap);
        worker.setWorkQueue(workQueue);
        for (int i = 0; i < workerCount; i++) {
            workers.put("子节点: " + i, new Thread(worker));
        }
    }

    // 提交任务
    public void submit(TaskDemo task) {
        workQueue.add(task);
    }

    // 启动所有的子任务
    public void execute(){
        for (Map.Entry<String, Thread> entry : workers.entrySet()) {
            entry.getValue().start();
        }
    }

    // 判断所有的任务是否执行结束
    public boolean isComplete() {
        for (Map.Entry<String, Thread> entry : workers.entrySet()) {
            if (entry.getValue().getState() != Thread.State.TERMINATED) {
                return false;
            }
        }

        return true;
    }

    // 获取最终汇总的结果
    public int getResult() {
        int result = 0;
        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            result += Integer.parseInt(entry.getValue().toString());
        }

        return result;
    }

}