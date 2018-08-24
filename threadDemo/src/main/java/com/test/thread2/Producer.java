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

    /*Exchanger（交换者）是一个用于线程间协作的工具类。Exchanger用于进行线程间的数据交换
    。它提供一个同步点，在这个同步点两个线程可以交换彼此的数据。这两个线程通过exchange方法交换数据，
    如果第一个线程先执行exchange方法，它会一直等待第二个线程也执行exchange，当两个线程都到达同步点时，
    这两个线程就可以交换数据，将本线程生产出来的数据传递给对方。因此使用Exchanger的重点是成对的线程使用exchange()方法，
    当有一对线程达到了同步点，就会进行交换数据。因此该工具类的线程对象是成对的。*/

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
