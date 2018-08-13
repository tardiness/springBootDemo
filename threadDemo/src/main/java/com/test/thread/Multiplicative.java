package com.test.thread;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/7/20
 * @modifyDate: 9:50
 * @Description:
 */
public class Multiplicative {

    public void testMultip() {
        Thread[] threads = new Thread[10];
        Thread.State[] states = new Thread.State[10];
        for (int i = 0; i < 10; i++) {

            threads[i] = new Thread((new Runnable() {
                @Override
                public void run() {
                    for (int j=0;j<=10;j++) {
                        for (int k = 0;k<=10;k++) {
                            System.out.printf("%s: %d * %d = %d\n",Thread.currentThread().getName(),j,k,j*k);
                        }
                    }
                }
            }));

            if ((i%2)==0) {
                threads[i].setPriority(Thread.MAX_PRIORITY);
            } else {
                threads[i].setPriority(Thread.MIN_PRIORITY);
            }

            threads[i].setName("Thread" + i);

            try {
                FileWriter writer = new FileWriter("D:/testOutPut/thread1.txt");
                PrintWriter printWriter = new PrintWriter(writer);
                printWriter.println("Main : Status of Thread "+i+" : " +threads[i].getState());
                printWriter.flush();
                writer.close();
                printWriter.close();

                states[i] = threads[i].getState();
            } catch (IOException e) {
                e.printStackTrace();
            }


            threads[i].start();
        }

        boolean finish = false;
        while (!finish) {

            try {
                FileWriter writer = new FileWriter("D:/testOutPut/thread1.txt");
                PrintWriter printWriter = new PrintWriter(writer);
                for (int i=0;i<10;i++) {
                    if (threads[i].getState() != states[i]) {
                        printWriter.printf("Main : Id %d - %s\n", threads[i].getId(), threads[i].getName());
                        printWriter.printf("Main : Priority: %d\n", threads[i].getPriority());
                        printWriter.printf("Main : Old State: %s\n", states[i]);
                        printWriter.printf("Main : New State: %s\n", threads[i].getState());
                        printWriter.printf("Main : ************************************\n");
                    }
                }
                printWriter.flush();
                writer.close();
                printWriter.close();

            }catch (IOException e) {
                e.printStackTrace();
            }

            finish = true;
            for (int i=0;i<10;i++) {
                finish = finish && (threads[i].getState() == Thread.State.TERMINATED);
            }
        }
    }
}
