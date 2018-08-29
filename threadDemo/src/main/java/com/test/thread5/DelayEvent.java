package com.test.thread5;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/29
 * @modifyDate: 9:12
 * @Description:
 */
public class DelayEvent implements Delayed {

    private Date startDate;

    public DelayEvent(Date startDate) {
        this.startDate = startDate;
    }

    @Override
    public long getDelay(TimeUnit unit) {
        Date now = new Date();
        long diff = startDate.getTime() - now.getTime();
        return unit.convert(diff,TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(Delayed o) {
        long result = this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS);
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        DelayQueue<DelayEvent> queue = new DelayQueue<>();
        Thread[] threads = new Thread[5];
        for (int i=0;i<threads.length;i++) {
            DelayTask task = new DelayTask(i,queue);
            threads[i] = new Thread(task);
        }

        for (int i=0;i<threads.length;i++) {
            threads[i].start();
        }

        do {
            int counter=0;
            DelayEvent event;
            do {
                event = queue.poll();
                if (event != null) counter++;

            } while (event!=null);
            System.out.printf("At %s you have read %d events \n",new Date(),counter);
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (queue.size()>0);
    }
}

class DelayTask implements Runnable {

    private int id;
    private DelayQueue<DelayEvent> queue;

    public DelayTask(int id, DelayQueue<DelayEvent> queue) {
        this.id = id;
        this.queue = queue;
    }

    @Override
    public void run() {
        Date now = new Date();
        Date delay = new Date();
        delay.setTime(now.getTime()+(id*1000));
        System.out.printf("Thread %s : %s \n",id,delay);

        for (int i=0;i<100;i++) {
            DelayEvent event = new DelayEvent(delay);
            queue.add(event);
        }

    }
}