package com.test.thread2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/13
 * @modifyDate: 14:10
 * @Description:
 */
public class VideoConference implements Runnable {

    private final CountDownLatch countDownLatch;

    public VideoConference(int number) {
        this.countDownLatch = new CountDownLatch(number);
    }

    public void arrive(String name) {
        System.out.printf("%s has arrived.\n",name);
        this.countDownLatch.countDown();
        System.out.printf("VideoConference: waiting for %d participants.\n",countDownLatch.getCount());
    }


    @Override
    public void run() {
        System.out.printf("VideoConference : Initialization %d participants.\n",countDownLatch.getCount());
        try {
            countDownLatch.await();
            System.out.printf("VideoConference : All the participants have come \n");
            System.out.printf("VideoConference : Let's start ... \n");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        VideoConference videoConference = new VideoConference(10);
        Thread threadConference = new Thread(videoConference);
        threadConference.start();
        for (int i=0;i<10;i++) {
            Participant participant = new Participant(videoConference,"Participant" + i);
            Thread t = new Thread(participant);
            t.start();
        }
    }
}

class Participant implements Runnable {

    private VideoConference videoConference;
    private String name;

    public Participant(VideoConference videoConference,String name) {
        this.videoConference = videoConference;
        this.name = name;
    }

    @Override
    public void run() {
        long duration = (long) (Math.random()*10);
        try {
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        videoConference.arrive(this.name);
    }
}
