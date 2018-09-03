package com.test.thread6;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/9/3
 * @modifyDate: 9:23
 * @Description:
 */
public class ParkingCounter extends AtomicInteger {

    private int maxNumber;

    public ParkingCounter(int maxNumber) {
        set(0);
        this.maxNumber = maxNumber;
    }

    public boolean carIn() {
        for (;;) {
            int value = get();

            if (value == maxNumber) {
                System.out.printf("ParkingCounter : the parking lot is full \n");
                return false;
            } else {
                int newValue = value+1;
                boolean changed = compareAndSet(value,newValue);
                if (changed) {
                    System.out.printf("ParkingCounter : a car has entered .\n");
                    return true;
                }
            }
        }
    }

    public boolean carOut () {
        for (;;) {
            int value = get();
            if (value == 0) {
                System.out.printf("ParkingCounter : the parking lot is empty. \n");
                return false;
            } else {
                int newValue = value-1;
                boolean changed = compareAndSet(value,newValue);
                if (changed) {
                    System.out.printf("ParkingCounter : a car has out .\n");
                    return true;
                }
            }
        }
    }

    public static void main(String[] args) {
        ParkingCounter counter = new ParkingCounter(5);
        Sensor1 sensor1 = new Sensor1(counter);
        Sensor2 sensor2 = new Sensor2(counter);

        Thread thread = new Thread(sensor1);
        Thread thread1 = new Thread(sensor2);

        thread.start();
        thread1.start();

        try {
            thread.join();
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Main : number of cars : %d \n",counter.get());
        System.out.println("Main : end of program");
    }
}

class Sensor1 implements Runnable {

    private ParkingCounter counter;

    public Sensor1(ParkingCounter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        counter.carIn();
        counter.carIn();
        counter.carIn();
        counter.carIn();
        counter.carOut();
        counter.carOut();
        counter.carOut();
        counter.carIn();
        counter.carIn();
        counter.carIn();

    }
}

class Sensor2 implements Runnable {

    private ParkingCounter counter;

    public Sensor2(ParkingCounter counter) {
        this.counter = counter;
    }

    @Override
    public void run() {
        counter.carIn();
        counter.carOut();
        counter.carOut();
        counter.carIn();
        counter.carIn();
        counter.carIn();
        counter.carIn();
        counter.carIn();
        counter.carIn();
    }
}