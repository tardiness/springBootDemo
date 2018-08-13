package com.test.thread;

import java.util.ArrayDeque;
import java.util.Date;
import java.util.Deque;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/7/20
 * @modifyDate: 11:08
 * @Description:
 */
public class DaemonThread {

    public static void main(String[] args) {

        Deque<Event> events = new ArrayDeque<>();
        for (int i=0;i<3;i++) {
            WorkTask workTask = new WorkTask(events);
            workTask.start();
        }

        CleanerTask cleanerTask = new CleanerTask(events);
        cleanerTask.start();
    }



}

class WorkTask extends Thread {
    private Deque<Event> events;

    public WorkTask (Deque<Event> events) {
        this.events = events;
    }

    @Override
    public void run() {
        for (int i=1;i<=100;i++) {
            Event event = new Event();
            event.setDate(new Date());
            event.setEvent(String.format("the thread %s has generated an event",Thread.currentThread().getId()));
            events.addFirst(event);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}


class CleanerTask extends Thread {

    private Deque<Event> events;

    public CleanerTask (Deque<Event> events) {
        this.events = events;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            Date date = new Date();
            try {
                clean(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void clean(Date date) {
        long different;
        boolean delete;

        if (events.size() == 0) {
            return;
        }
        delete = false;
        do {
            Event event = events.getLast();
            different = date.getTime() - event.getDate().getTime();
            if (different > 10000) {
                System.out.printf("Cleaner : %s \n", event.getEvent());
                events.removeLast();
                delete = true;
            }
        } while (different > 10000);

        if (delete) {
            System.out.printf("Cleaner: Size of the queue: %d \n",events.size());
        }
    }
}



class Event{
    private Date date;
    private String event;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
