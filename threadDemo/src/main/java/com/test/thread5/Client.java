package com.test.thread5;

import java.util.Date;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class Client implements Runnable {

    private LinkedBlockingDeque<String> requestList;

    public Client(LinkedBlockingDeque<String> requestList) {
        this.requestList = requestList;
    }

    @Override
    public void run() {
        for (int i=0;i<3;i++) {
            for (int j=0;j<5;j++) {
                StringBuilder builder = new StringBuilder();
                builder.append(i).append(":").append(j);
                try {
                    requestList.put(builder.toString());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.printf("Client: %s at %s \n",builder,new Date());
            }

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("Client: end.\n");
    }

    public static void main(String[] args) {
        LinkedBlockingDeque<String> list = new LinkedBlockingDeque<>(3);
        Client client = new Client(list);
        Thread thread = new Thread(client);
        thread.start();

        for (int i=0;i<5;i++) {
            for (int j=0;j<3;j++) {
                try {
                    String request = list.take();
                    System.out.printf("Main : request %s at %s . size: %d \n",request,new Date(),list.size());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.printf("Main: end of program \n");
    }
}
