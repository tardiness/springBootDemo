package com.test.thread;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/7/20
 * @modifyDate: 11:59
 * @Description:
 */
public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.printf("An exception has been captured\n");
        System.out.printf("Thread is : %s\n",t.getId());
        System.out.printf("Exception: %s: %s",e.getClass().getName(),e.getMessage());
        System.out.printf("Stack Trace: \n");
        e.printStackTrace(System.out);
        System.out.printf("Thread status: %s \n",t.getState());
    }

    class Task implements Runnable{

        @Override
        public void run() {
            int numero = Integer.parseInt("TTT");
        }
    }

    public static void main(String[] args) {
        Task task = new ExceptionHandler().new Task();
        Thread thread = new Thread(task);
        thread.setUncaughtExceptionHandler(new ExceptionHandler());
        thread.start();
    }
}
