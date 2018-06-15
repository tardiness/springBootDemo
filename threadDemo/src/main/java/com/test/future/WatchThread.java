package com.test.future;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/6/15
 * @modifyDate: 16:37
 * @Description:
 */
public class WatchThread {

    private String name = UUID.randomUUID().toString();

    public void testThread() throws InterruptedException {
        int threadCount = 10;
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        for (int i=1;i<threadCount;i++) {
            TestThread testThread = new TestThread(countDownLatch);
            executor.execute(testThread);
        }
        countDownLatch.await();
        executor.shutdown();
    }

    class TestThread implements Runnable {

        private CountDownLatch countDownLatch;

        public TestThread(CountDownLatch countDownLatch) {
            this.countDownLatch = countDownLatch;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "开始..." + name);
            System.out.println("开始了线程：：：：" + countDownLatch.getCount());
            countDownLatch.countDown();//必须等核心处理逻辑处理完成后才可以减1
            System.out.println(Thread.currentThread().getName() + "结束. 还有"
                    + countDownLatch.getCount() + " 个线程");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        new WatchThread().testThread();
    }
}
