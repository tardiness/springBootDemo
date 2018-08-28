package com.test.thread4;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/28
 * @modifyDate: 11:15
 * @Description:
 */
public class FolderProcessor extends RecursiveTask<List<String>> {

    private static final long serialVersionUID = 3574629309533028109L;

    private String path;
    private String extension;

    public FolderProcessor(String path, String extension) {
        this.path = path;
        this.extension = extension;
    }

    public static void main(String[] args) {
        ForkJoinPool pool = new ForkJoinPool();
        FolderProcessor system = new FolderProcessor("C:\\Windows","log");
        FolderProcessor apps = new FolderProcessor("C:\\Program Files","log");
        pool.execute(system);
        pool.execute(apps);

        do {
            System.out.println("*********************************");
            System.out.printf("Main:Parallelism : %d \n",pool.getParallelism());
            System.out.printf("Main:active thread : %d \n",pool.getActiveThreadCount());
            System.out.printf("Main:task count : %d \n",pool.getQueuedTaskCount());
            System.out.printf("Main:steal count : %d \n",pool.getStealCount());
            System.out.println("*********************************");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!system.isDone() || !apps.isDone());

        pool.shutdown();

        List<String> results;
        results = system.join();
        System.out.printf("System : %d file found\n",results.size());
        results = apps.join();
        System.out.printf("Apps : %d file found\n",results.size());
    }


    @Override
    protected List<String> compute() {
        List<String> list = new ArrayList<>();
        List<FolderProcessor> processers = new ArrayList<>();

        File file = new File(path);
        File[] content = file.listFiles();
        if (content != null) {
            for (int i=0;i<content.length;i++) {
                if (content[i].isDirectory()) {
                    FolderProcessor processor = new FolderProcessor(content[i].getAbsolutePath(),extension);
                    processor.fork();
                    processers.add(processor);
                } else {
                    if (checkFile(content[i].getName())) {
                        list.add(content[i].getAbsolutePath());
                    }
                }
            }
        }
        if (processers.size() > 50) {
            System.out.printf("%s : %d tasks ran.\n",file.getAbsolutePath(),processers.size());
        }
        addResultFromTask(list,processers);

        return list;
    }

    private void addResultFromTask(List<String> list, List<FolderProcessor> processers) {
        for (FolderProcessor processor: processers) {
            list.addAll(processor.join());
        }
    }

    private boolean checkFile(String name) {
        return name.endsWith(extension);
    }
}
