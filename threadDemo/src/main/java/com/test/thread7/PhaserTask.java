package com.test.thread7;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/3
 * @modifyDate: 10:11
 * @Description:
 */
public class PhaserTask implements Runnable {

    private int time;
    private Phaser phaser;

    public PhaserTask(int time, Phaser phaser) {
        this.time = time;
        this.phaser = phaser;
    }

    @Override
    public void run() {
        phaser.arrive();
        System.out.printf("%s : entering phase 1.\n",Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s : finished phase 1\n",Thread.currentThread().getName());
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s : entering phase 2.\n",Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s : finished phase 2\n",Thread.currentThread().getName());
        phaser.arriveAndAwaitAdvance();

        System.out.printf("%s : entering phase 3.\n",Thread.currentThread().getName());
        try {
            TimeUnit.SECONDS.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("%s : finished phase 3\n",Thread.currentThread().getName());
        phaser.arriveAndDeregister();

    }

    public static void main(String[] args) {
        Phaser phaser = new Phaser(3);
        for (int i=0;i<3;i++) {
            PhaserTask task = new PhaserTask(i,phaser);
            Thread thread = new Thread(task);
            thread.start();
        }

        for (int i=0;i<10;i++) {
            System.out.println("****************************");
            System.out.printf("Main : phaser log\n");
            System.out.printf("Main : phaser : phase : %d\n",phaser.getPhase());
            System.out.printf("Main : phaser : registered parties: %d \n",phaser.getPhase());
            System.out.printf("Main : phaser : arrived parties: %d \n ",phaser.getArrivedParties());
            System.out.printf("Main : phaser : unArrived parties: %d \n ",phaser.getUnarrivedParties());
            System.out.println("****************************");

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
