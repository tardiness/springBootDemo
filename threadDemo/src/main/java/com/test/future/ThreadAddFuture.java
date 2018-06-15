package com.test.future;



import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/6/15
 * @modifyDate: 16:09
 * @Description:
 */

public class ThreadAddFuture {

    public static List<Future> futures = new ArrayList<>();

    class ThreadTest implements Callable<Integer> {

        private int begin;

        private int end;

        private int sum;

        public ThreadTest (int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        @Override
        public Integer call() throws Exception {
            System.out.println(begin+","+end);
            for (int i=begin;i <= end;i++) {
                sum += i;
            }
            System.out.println("from" + Thread.currentThread().getName()+ ",sum="+ sum);
            return sum;
        }
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int sum = 0;
        int taskSize = 4;

        ThreadAddFuture addFuture = new ThreadAddFuture();
        ExecutorService pool = Executors.newFixedThreadPool(taskSize);
        for (int i = 1; i <= 76;) {
            ThreadTest threadTest = addFuture.new ThreadTest(i, i+24);
            Future<Integer> future = pool.submit(threadTest);
            futures.add(future);
            i+=25;
        }
        if (futures.size()>0 || futures != null) {
            for (Future future:futures) {
                sum += (Integer) future.get();
            }
        }
        System.out.println("total result:" + sum);
        pool.shutdown();
    }
}
