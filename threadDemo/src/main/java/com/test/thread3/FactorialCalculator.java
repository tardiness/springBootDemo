package com.test.thread3;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/23
 * @modifyDate: 11:39
 * @Description:
 */
public class FactorialCalculator implements Callable<Integer> {

    private Integer number;

    public FactorialCalculator(Integer number) {
        this.number = number;
    }

    public static void main(String[] args) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(2);
        List<Future<Integer>> resultList = new ArrayList<>();
        Random random = new Random();
        for (int i=0;i<10;i++) {
            Integer number = random.nextInt(10);
            FactorialCalculator calculator = new FactorialCalculator(number);
            Future<Integer> result = executor.submit(calculator);
            resultList.add(result);
        }
        do {
            System.out.printf("Main: Number of complete Tasks: %d\n",executor.getCompletedTaskCount());
            for (int i=0;i <resultList.size();i++) {
                Future<Integer> result = resultList.get(i);
                System.out.printf("Main: task %d : %s \n",i,result.isDone());
            }
            try {
                TimeUnit.MILLISECONDS.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (executor.getCompletedTaskCount() < resultList.size());

        System.out.printf("Main: result \n");
        for (int i=0;i<resultList.size();i++) {
            Future<Integer> result = resultList.get(i);
            Integer number = null;
            try {
                number = result.get();
            } catch (InterruptedException e) {
                //等待结果是线程中断
                e.printStackTrace();
            } catch (ExecutionException e) {
                //call 方法内部错误
                e.printStackTrace();
            }
            System.out.printf("Main: task %d : %s\n",i,number);

        }

        executor.shutdown();

    }

    @Override
    public Integer call() throws Exception {
        int result = 1;
        if (number == 0 || number == 1) {
            return 1;
        } else {
            for (int i=2;i< number;i++) {
                result *= i;
                TimeUnit.MILLISECONDS.sleep(20);
            }
        }
        System.out.printf("%s: %d \n",Thread.currentThread().getName(),result);
        return result;
    }
}
