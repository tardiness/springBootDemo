package com.test.thread;

import java.io.File;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/7/20
 * @modifyDate: 10:00
 * @Description:
 */
public class FileSearch implements Runnable {

    private String fileName;
    private String initPath;

    public FileSearch(String fileName, String initPath) {
        this.fileName = fileName;
        this.initPath = initPath;
    }

    public static void main(String[] args) {
        FileSearch fileSearch = new FileSearch("thread1.txt","D:/testOutPut/");
        Thread thread = new Thread(fileSearch);
        thread.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }

    @Override
    public void run() {
        File file = new File(initPath);
        if (file.isDirectory()) {
            try {
                directoryProcess(file);
            } catch (InterruptedException e) {
                System.out.printf("%s: The search has been interrupted",Thread.currentThread().getName());
            }
        }
    }

    private void directoryProcess(File file) throws InterruptedException {
        File[] list = file.listFiles();
        if (list != null) {
            for (int i = 0; i < list.length ;i++) {
                if (list[i].isDirectory()) {
                    directoryProcess(list[i]);
                } else {
                    fileProcess(list[i]);
                }
            }
        }
    }

    private void fileProcess(File file) throws InterruptedException {
        if (file.getName().equals(fileName)) {
            System.out.printf("%s : %s\n",Thread.currentThread().getName() ,file.getAbsolutePath());
        } else {
            throw new InterruptedException();
        }
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getInitPath() {
        return initPath;
    }

    public void setInitPath(String initPath) {
        this.initPath = initPath;
    }
}
