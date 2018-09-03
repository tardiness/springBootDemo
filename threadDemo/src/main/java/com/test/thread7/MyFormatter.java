package com.test.thread7;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/3
 * @modifyDate: 11:05
 * @Description:
 */
public class MyFormatter extends Formatter {


    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();
        builder.append("["+record.getLevel()+"] - ");
        builder.append(new Date(record.getMillis()) + ":");
        builder.append(record.getSourceClassName() + "."+record.getSourceMethodName()+":");
        builder.append(record.getMessage()+"\n");
        return builder.toString();
    }

    public static void main(String[] args) {
        Logger logger = MyLogger.getLogger("core");
        logger.entering("core","main()",args);
        Thread[] threads = new Thread[5];

        for (int i=0;i<threads.length;i++) {
            logger.log(Level.INFO,"Launching thread :"+i);
            LogTask task = new LogTask();
            threads[i] = new Thread(task);
            logger.log(Level.INFO,"Thread created :" + threads[i]);
            threads[i].start();
        }

        logger.log(Level.INFO,"Ten Threads created ." + "waiting for its finalization");

        for (int i=0;i<threads.length;i++) {
            try {
                threads[i].join();
                logger.log(Level.INFO,"Thread has finished its execution ",threads[i]);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE,"Exception",e);
            }
        }
        logger.exiting("core","main()");
    }
}
class MyLogger {

    private static Handler handler;

    public static Logger getLogger(String name) {
        Logger logger = Logger.getLogger(name);
        logger.setLevel(Level.ALL);


        try {
            if (handler == null) {
                handler = new FileHandler("recipe8.log");
                Formatter formatter = new MyFormatter();
                handler.setFormatter(formatter);
            }
            if (logger.getHandlers().length == 0) {
                logger.addHandler(handler);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return logger;
    }

}

class LogTask implements Runnable {


    @Override
    public void run() {
        Logger logger = MyLogger.getLogger(this.getClass().getName());

        logger.entering(Thread.currentThread().getName(),"run()");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.exiting(Thread.currentThread().getName(),"run()",Thread.currentThread());
    }
}