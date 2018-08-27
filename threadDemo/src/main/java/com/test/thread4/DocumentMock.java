package com.test.thread4;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class DocumentMock {

    private String[] words = {"the","hello","goodbye","packet","java","thread","pool","random","class","main"};

    public String[][] generateDocument(int numLines, int numWords,String word) {
        int counter = 0;
        String[][] document = new String[numLines][numWords];
        Random random = new Random();
        for (int i=0;i<numLines;i++) {
            for (int j=0;j<numWords;j++) {
                int index =random.nextInt(word.length());
                document[i][j] = words[index];
                if (document[i][j].equals(word) ) {
                    counter++;
                }
            }
        }
        System.out.printf("DocumentMock : the word appears %d times in the document \n",counter);
        return document;
    }

    public static void main(String[] args) {
        DocumentMock documentMock = new DocumentMock();
        String[][] document = documentMock.generateDocument(100,1000,"the");
        DocumentTask documentTask = new DocumentTask(document,0,100,"the");
        ForkJoinPool pool = new ForkJoinPool();
        pool.execute(documentTask);

        do {
            System.out.println("********************************************");
            System.out.printf("Main: parallelism :%d \n",pool.getParallelism());
            System.out.printf("Main: active thread:%d \n",pool.getActiveThreadCount());
            System.out.printf("Main: taskCount :%d \n",pool.getQueuedTaskCount());
            System.out.printf("Main: steal count :%d \n",pool.getStealCount());
            System.out.println("********************************************");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (!documentTask.isDone());

        pool.shutdown();
        try {
            pool.awaitTermination(1,TimeUnit.DAYS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            System.out.printf("Main : the word appears %s in the document",documentTask.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}

class DocumentTask extends RecursiveTask<Integer> {

    private String[][] document;
    private int start,end;
    private String word;

    public DocumentTask(String[][] document, int start, int end, String word) {
        this.document = document;
        this.start = start;
        this.end = end;
        this.word = word;
    }

    @Override
    protected Integer compute() {
        int result = 0;
        if (end - start < 10) {
            result = processLines(document,start,end,word);
        } else {
            int mid = (start+end)/2;
            DocumentTask documentTask1 = new DocumentTask(document,start,mid,word);
            DocumentTask documentTask2 = new DocumentTask(document,mid,end,word);
            invokeAll(documentTask1,documentTask2);
            try {
                result = groupResults(documentTask1.get(),documentTask2.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Integer groupResults(Integer integer, Integer integer1) {
        Integer result = integer + integer1;
        return result;
    }

    private Integer processLines(String[][] document, int start, int end, String word) {
        List<LineTask> lineTasks = new ArrayList<>();
        for (int i=start;i < end;i++) {
            LineTask lineTask = new LineTask(document[i],0,document[i].length,word);
            lineTasks.add(lineTask);
        }
        invokeAll(lineTasks);

        int result=0;
        for (int i =0; i< lineTasks.size();i++) {
            LineTask lineTask = lineTasks.get(i);
            try {
                result = result + lineTask.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }
}

class LineTask extends RecursiveTask<Integer> {

    private static final long serialVersionUID = 1635787333121146988L;

    private String[] line;
    private int start,end;
    private String word;

    public LineTask(String[] line, int start, int end, String word) {
        this.line = line;
        this.start = start;
        this.end = end;
        this.word = word;
    }

    @Override
    protected Integer compute() {
        Integer result = 0;
        if (end - start < 100) {
            result = count(line,start,end,word);
        } else {
            int mid = (start+ end)/2;
            LineTask lineTask1 = new LineTask(line,start,mid,word);
            LineTask lineTask2 = new LineTask(line,mid,end,word);
            invokeAll(lineTask1,lineTask2);
            try {
                result = groupResult(lineTask1.get(),lineTask2.get());
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private Integer count(String[] line, int start, int end, String word) {
        Integer counter = 0;
        for (int i=start;i<end;i++) {
            if (line[i].equals(word)) {
                counter++;
            }
        }
        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return counter;
    }

    private Integer groupResult(Integer num1, Integer num2) {
        Integer result = num1 + num2;
        return result;
    }
}
