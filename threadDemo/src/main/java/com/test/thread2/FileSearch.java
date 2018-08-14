package com.test.thread2;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/13
 * @modifyDate: 16:55
 * @Description:
 */
public class FileSearch implements Runnable {

    private String initPath;
    private String end;
    private List<String> results;
    private Phaser phaser;

    public FileSearch(String initPath, String end, Phaser phaser) {
        this.initPath = initPath;
        this.end = end;
        this.results = new ArrayList<>();
        this.phaser = phaser;
    }

    public static void main(String[] args) {
        Phaser phaser1 = new Phaser(3);
        FileSearch system = new FileSearch("C:\\Windows","log",phaser1);
        FileSearch apps = new FileSearch("C:\\Program Files","log",phaser1);
        FileSearch document = new FileSearch("C:\\Users\\Administrator","log",phaser1);

        Thread thread = new Thread(system,"System");
        thread.start();
        Thread thread1 = new Thread(apps,"Apps");
        thread1.start();
        Thread thread2 = new Thread(document,"Document");
        thread2.start();

        try {
            thread.join();
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.printf("Terminated : "+ phaser1.isTerminated());
    }

    @Override
    public void run() {
        phaser.arriveAndAwaitAdvance();
        System.out.printf("%s : Starting . \n",Thread.currentThread().getName());
        File file = new File(initPath);
        if (file.isDirectory()) {
            directoryProcess(file);
        }

        if (!checkResult()) {
            return;
        }
        filterResults();
        if (!checkResult()) {
            return;
        }
        showInfo();
        phaser.arriveAndDeregister();
        System.out.printf("%s : work completed. \n",Thread.currentThread().getName());
    }

    private void directoryProcess(File file) {
        File[] list = file.listFiles();
        if (list != null) {
            for (int i=0;i<list.length;i++) {
                if (list[i].isDirectory()) {
                    directoryProcess(list[i]);
                } else {
                    fileProcess(list[i]);
                }
            }
        }
    }

    private void fileProcess(File file) {
        if (file.getName().endsWith(end)) {
            results.add(file.getAbsolutePath());
        }
    }

    private void filterResults() {
        List<String> newResults = new ArrayList<>();
        long actualDate = new Date().getTime();
        for (int i=0;i<results.size();i++) {
            File file = new File(results.get(i));
            long fileDate = file.lastModified();
            if (actualDate-fileDate < TimeUnit.MILLISECONDS.convert(1,TimeUnit.DAYS)) {
                newResults.add(results.get(i));
            }
        }
        results = newResults;
    }

    private boolean checkResult() {
        if (results.isEmpty()) {
            System.out.printf("%s:Phase %d : 0 results \n",Thread.currentThread().getName(),phaser.getPhase());
            System.out.printf("%s :Phase %d:end. \n",Thread.currentThread().getName(),phaser.getPhase());
            //完成当前阶段  不再参与下面的阶段
            phaser.arriveAndDeregister();
            return false;
        } else {
            System.out.printf("%s : Phase %d :%d results \n",Thread.currentThread().getName(),phaser.getPhase(),results.size());
            //完成当前阶段  等待参与下面的阶段
            phaser.arriveAndAwaitAdvance();
            return true;
        }
    }

    private void showInfo() {
        for (int i=0;i<results.size();i++) {
            File file = new File(results.get(i));
            System.out.printf("%s : %s \n",Thread.currentThread().getName(),file.getAbsolutePath());
        }
        phaser.arriveAndAwaitAdvance();
    }
}
