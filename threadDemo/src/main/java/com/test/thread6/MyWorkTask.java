package com.test.thread6;

import java.util.Date;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/1
 * @modifyDate: 15:35
 * @Description:
 */
public abstract class MyWorkTask extends ForkJoinTask<Void> {

    private String name;

    public MyWorkTask(String name) {
        this.name = name;
    }

    @Override
    public Void getRawResult() {
        return null;
    }

    @Override
    protected void setRawResult(Void value) {

    }

    @Override
    protected boolean exec() {
        Date startDate = new Date();
        compute();
        Date finishDate = new Date();
        long diff = finishDate.getTime() - startDate.getTime();
        System.out.printf("MyWorkTask : %s : %d milliseconds to complete.\n ",name,diff );
        // false 会导致卡住 目前不知道为什么
        return true;
    }

    public String getName() {
        return name;
    }

    protected abstract void compute();

    public static void main(String[] args) {
        int[] array = new int[10000];
        ForkJoinPool pool = new ForkJoinPool();
        WorkTask task = new WorkTask("Task",array,0,array.length);
        pool.invoke(task);
        pool.shutdown();
        System.out.println("Main : end of program");
    }
}

class WorkTask extends MyWorkTask {

    private int[] array;
    private int start,end;

    public WorkTask(String name, int[] array, int start, int end) {
        super(name);
        this.array = array;
        this.start = start;
        this.end = end;
    }

    @Override
    protected void compute() {
        if (end-start>100) {
            int mid = (start+end)/2;
            WorkTask task = new WorkTask(this.getName()+"1",array,start,mid);
            WorkTask task1 = new WorkTask(this.getName()+"2",array,mid,end);
            invokeAll(task,task1);
        } else {
            for (int i=start;i<end;i++) {
                array[i]++;
            }
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
