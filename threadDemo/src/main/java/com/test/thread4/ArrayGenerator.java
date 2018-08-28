package com.test.thread4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/28
 * @modifyDate: 16:31
 * @Description:
 */
public class ArrayGenerator {

    public int[] generateArray(int size) {
        int[] array = new int[size];
        Random random = new Random();
        for (int i=0;i<size;i++) {
            array[i] = random.nextInt(10);
        }
        return array;
    }

    public static void main(String[] args) {
        ArrayGenerator generator = new ArrayGenerator();
        int[] array = generator.generateArray(1000);
        TaskManager manager = new TaskManager();
        ForkJoinPool pool = new ForkJoinPool();
        SearchNumberTask task = new SearchNumberTask(array,0,1000,5,manager);
        pool.execute(task);
        pool.shutdown();
        try {
            pool.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main: the program has finished \n");
    }


}

class TaskManager {

    private List<ForkJoinTask<Integer>> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    public void addTask(ForkJoinTask<Integer> task) {
        tasks.add(task);
    }

    public void cancelTasks(ForkJoinTask<Integer> cancelTask) {
        for (ForkJoinTask<Integer> task:tasks) {
            if (task != cancelTask) {
                task.cancel(true);
                ((SearchNumberTask)task).writeCancelMessage();
            }
        }
    }
}
class SearchNumberTask extends RecursiveTask<Integer> {

    private static final long serialVersionUID = -309803125583486429L;

    private static final int NOT_FOUND = -1;

    private int[] numbers;
    private int start,end;
    private int number;
    private TaskManager taskManager;

    public SearchNumberTask(int[] numbers, int start, int end, int number, TaskManager taskManager) {
        this.numbers = numbers;
        this.start = start;
        this.end = end;
        this.number = number;
        this.taskManager = taskManager;
    }

    @Override
    protected Integer compute() {
        System.out.printf("Task : %s : %s \n",start,end);
        int ret;
        if (end-start > 10) {
            ret = launchTasks();
        } else {
            ret = lookForNumber();
        }
        return ret;
    }

    private int lookForNumber() {
        for (int i=start;i<end;i++) {
            if (numbers[i] == number) {
                System.out.printf("Task : number %d found in position %d \n",number,i);
                taskManager.cancelTasks(this);
                return i;
            }
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return NOT_FOUND;
    }

    private int launchTasks() {
        int mid = (start+end)/2;
        SearchNumberTask task1 = new SearchNumberTask(numbers,start,mid,number,taskManager);
        SearchNumberTask task2 = new SearchNumberTask(numbers,mid,end,number,taskManager);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        task1.fork();
        task2.fork();
        int returnValue = 0;
        returnValue = task1.join();
        if (returnValue != -1) {
            return returnValue;
        }
        returnValue = task2.join();
        return returnValue;
    }

    public void writeCancelMessage() {
        System.out.printf("Task: cancelled task from %d to %d \n", start, end);
    }
}
