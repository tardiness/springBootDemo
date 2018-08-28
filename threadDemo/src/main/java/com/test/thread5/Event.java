package com.test.thread5;

import java.util.concurrent.PriorityBlockingQueue;

public class Event implements Comparable<Event> {

    private int thread;
    private int priority;

    public Event(int thread, int priority) {
        this.thread = thread;
        this.priority = priority;
    }

    public int getThread() {
        return thread;
    }

    public int getPriority() {
        return priority;
    }

    @Override
    public int compareTo(Event o) {
        if (this.priority > o.getPriority()) {
            return -1;
        } else if (this.priority < o.getPriority()) {
            return 1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        PriorityBlockingQueue<Event> queue = new PriorityBlockingQueue<>();
        Thread[] threads = new Thread[5];
        for (int i=0;i<threads.length;i++) {
            PriorityTask priorityTask = new PriorityTask(i,queue);
            threads[i] = new Thread(priorityTask);
        }

        for (int i=0;i<threads.length;i++) {
            threads[i].start();
        }

        for (int i=0;i<threads.length;i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Main : queue Size : %d \n",queue.size());
        for (int i=0;i<threads.length*1000;i++) {
            Event event = queue.poll();
            System.out.printf("Thread %s : priority %d \n",event.getThread(),event.getPriority());
        }

        System.out.printf("Main : queue Size : %d \n",queue.size());
        System.out.printf("Main : end of program \n");

    }
}

class PriorityTask implements Runnable {

    private int id;
    private PriorityBlockingQueue<Event> queue;

    public PriorityTask(int id, PriorityBlockingQueue<Event> queue) {
        this.id = id;
        this.queue = queue;
    }

    @Override
    public void run() {
        for (int i=0;i<1000;i++) {
            Event event = new Event(id,i);
            queue.add(event);
        }
    }
}
