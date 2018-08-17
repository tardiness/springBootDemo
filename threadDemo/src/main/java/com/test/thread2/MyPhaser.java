package com.test.thread2;

import java.util.Date;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/16
 * @modifyDate: 14:50
 * @Description:
 */
public class MyPhaser extends Phaser {

    public static void main(String[] args) {
        MyPhaser myPhaser = new MyPhaser();
        Student[] students = new Student[5];
        for (int i=0;i<5;i++) {
            students[i] = new Student(myPhaser);
            myPhaser.register();
        }
        Thread[] threads = new Thread[5];
        for (int i=0;i<threads.length;i++) {
            threads[i] = new Thread(students[i]);
            threads[i].start();
        }

        for (int i=0;i<threads.length;i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Main: phaser has finished : %s",myPhaser.isTerminated());
    }

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        switch (phase) {
            case 0:
                return studentsArrived();
            case 1:
                return finishFirstExercise();
            case 2:
                return finishSecondExercise();
            case 3:
                return finishExam();
            default:
                return true;
        }
    }

    private boolean finishExam() {
        System.out.printf("Phaser: all students have finished exam.\n");
        System.out.printf("Phaser: thank for your time .\n");
        return true;
    }

    private boolean finishSecondExercise() {
        System.out.printf("Phaser: have finished second exercise.\n");
        System.out.printf("Phaser: it's time to start third one.\n");
        return false;
    }

    private boolean finishFirstExercise() {
        System.out.printf("Phaser: have finished first exercise.\n");
        System.out.printf("Phaser: it's time to start second .\n");
        return false;
    }

    private boolean studentsArrived() {
        System.out.printf("Phaser: The exam are going to start,the student is ready.\n");
        System.out.printf("Phaser: we have %s students.\n",getRegisteredParties());
        return false;
    }
}

class Student implements Runnable {

    private Phaser phaser;

    public Student(Phaser phaser) {
        this.phaser = phaser;
    }

    @Override
    public void run() {
        System.out.printf("%s: has arrived to do the exam. %s \n",Thread.currentThread().getName(),new Date());
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: is going to do the first exercise.%s \n",Thread.currentThread().getName(),new Date());
        doExericse1();
        System.out.printf("%s: has done the first exercise.%s \n",Thread.currentThread().getName(),new Date());
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: is going to do the second exercise.%s \n",Thread.currentThread().getName(),new Date());
        doExericse2();
        System.out.printf("%s: has done the second exercise.%s \n",Thread.currentThread().getName(),new Date());
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s: is going to do the third exercise.%s \n",Thread.currentThread().getName(),new Date());
        doExericse3();
        System.out.printf("%s: has done the third exercise.%s \n",Thread.currentThread().getName(),new Date());
        phaser.arriveAndAwaitAdvance();
    }

    private void doExericse3() {
        try {
            long duration = (long) (Math.random() * 10);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doExericse2() {
        try {
            long duration = (long) (Math.random() * 10);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void doExericse1() {
        try {
            long duration = (long) (Math.random() * 10);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}