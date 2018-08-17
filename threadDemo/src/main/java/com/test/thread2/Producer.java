package com.test.thread2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Exchanger;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/16
 * @modifyDate: 15:46
 * @Description:  exchanger  交换数据
 * 例如： producer 有10条数据  consumer 中 没有数据  ； 经过 exchanger producer 中没数据 consumer 中 10条数据
 */
public class Producer implements Runnable {

    private List<String> buffer;
    private final Exchanger<List<String>> exchanger;

    public Producer(List<String> buffer, Exchanger<List<String>> exchanger) {
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    public static void main(String[] args) {
        List<String> buffer1 = new ArrayList<>();
        List<String> buffer2 = new ArrayList<>();

        Exchanger<List<String>> exchanger = new Exchanger<>();
        Producer producer = new Producer(buffer1,exchanger);
        Consumer consumer = new Consumer(buffer2,exchanger);

        Thread thread = new Thread(producer);
        Thread thread1 = new Thread(consumer);
        thread.start();
        thread1.start();
    }

    @Override
    public void run() {

        int cycle = 1;
        for (int i=0;i<10;i++) {
            System.out.printf("Producer:cycle %d\n",cycle);
            for (int j=0;j<10;j++) {
                String message = "event"+ (i * 10 + j);
                System.out.printf("Producer: %s \n",message);
                buffer.add(message);
            }

            try {
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Producer :"+ buffer.size());
            cycle++;
        }
    }
}

class Consumer implements Runnable {

    private List<String> buffer;
    private final Exchanger<List<String>> exchanger;

    public Consumer(List<String> buffer, Exchanger<List<String>> exchanger) {
        this.buffer = buffer;
        this.exchanger = exchanger;
    }

    @Override
    public void run() {
        int cycle = 1;
        for (int i=0;i< 10;i++) {
            System.out.printf("Consumer: cycle %d\n",cycle);
            try {
                buffer = exchanger.exchange(buffer);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Consumer: "+ buffer.size());
            for (int j=0;j<10;j++) {
                String message = buffer.get(0);
                System.out.println("Consumer : " + message);
                buffer.remove(0);
            }
            cycle++;
        }
    }
}
