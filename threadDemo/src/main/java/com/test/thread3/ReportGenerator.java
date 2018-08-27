package com.test.thread3;

import java.util.concurrent.*;

public class ReportGenerator implements Callable<String> {

    private String sender;
    private String title;

    public ReportGenerator(String sender, String title) {
        this.sender = sender;
        this.title = title;
    }

    @Override
    public String call() throws Exception {
        try {
            long duration = (long) (Math.random() * 10);
            System.out.printf("%s_%s:ReportGenerator:generating a report duration %s seconds\n",sender,title,duration);
            TimeUnit.SECONDS.sleep(duration);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String ret = sender +":"+ title;
        return ret;
    }

    public static void main(String[] args) {
        ExecutorService service = Executors.newCachedThreadPool();
        CompletionService<String> completionService = new ExecutorCompletionService<>(service);
        ReportRequest faceRequest = new ReportRequest("face",completionService);
        ReportRequest onLineRequest = new ReportRequest("onLine",completionService);

        Thread faceThread = new Thread(faceRequest);
        Thread onLineThread = new Thread(onLineRequest);

        ReportProcessor processor = new ReportProcessor(completionService);
        Thread processorThread = new Thread(processor);

        System.out.printf("Main: Starting the  threads\n");
        faceThread.start();
        onLineThread.start();
        processorThread.start();

        try {
            System.out.printf("Main:waiting for request generators\n");
            faceThread.join();
            onLineThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.printf("Main:shutdown the executor.\n");
        service.shutdown();

        try {
            service.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        processor.setEnd(true);
        System.out.printf("Main:ends\n");
    }
}
class ReportRequest implements Runnable {

    private String name;
    private CompletionService<String> service;

    public ReportRequest(String name, CompletionService<String> service) {
        this.name = name;
        this.service = service;
    }

    @Override
    public void run() {
        ReportGenerator generator = new ReportGenerator(name,"Report");
        service.submit(generator);
    }
}
class ReportProcessor implements Runnable {

    private CompletionService<String> service;
    private boolean end;

    public ReportProcessor(CompletionService<String> service) {
        this.service = service;
        this.end = false;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    @Override
    public void run() {
        while (!end) {
            try {
                Future<String> result = service.poll(20,TimeUnit.SECONDS);
                if (result!= null) {
                    String report = result.get();
                    System.out.printf("ReportReceiver: Report Received: %s\n",report);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        System.out.printf("ReportSender: End\n");
    }
}
