package com.test.thin.test;

import java.util.concurrent.*;

/**
 * @author: shishaopeng
 * @project: springbootdemo
 * @data: 2018/6/11
 * @modifyDate: 16:41
 * @Description:
 */
public class FutureDemo2 {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ExecutorService service = Executors.newCachedThreadPool();
        Future<String> future = service.submit(new RealData2());

        System.out.println("RealData2方法调用完毕");
        // 模拟主函数中其他耗时操作
        doOtherThing();
        // 获取RealData2方法的结果
        System.out.println(future.get());
    }

    private static void doOtherThing() throws InterruptedException {
        Thread.sleep(2000L);
    }
}

class RealData2 implements Callable<String> {

    public String costTime() {
        try {
            // 模拟RealData耗时操作
            Thread.sleep(1000L);
            return "result";
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "exception";
    }

    @Override
    public String call() throws Exception {
        return costTime();
    }
}