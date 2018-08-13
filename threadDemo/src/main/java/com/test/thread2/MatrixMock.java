package com.test.thread2;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/13
 * @modifyDate: 14:49
 * @Description:
 */
public class MatrixMock {

    private int[][] data;

    public MatrixMock(int size,int length,int number) {
        int counter = 0;
        data = new int[size][length];
        Random random = new Random();
        for (int i=0;i<size;i++) {
            for (int j=0;j<length;j++) {
                data[i][j] = random.nextInt(10);
                if (data[i][j] == number)
                    counter++;
            }
        }
        System.out.printf("Mock: there are %d ocurrences of number in generated data.\n",counter,number);
    }

    public int[] getRow(int row) {
        if ((row>=0)&&(row<data.length)) {
            return data[row];
        }
        return null;
    }

    public static void main(String[] args) {
        final int ROWS = 10000;
        final int NUMBER = 1000;
        final int SEARCH = 7;
        final int PARTICIPANT = 5;
        final int LINES_PARTICIPANT = 2000;

        MatrixMock mock = new MatrixMock(ROWS,NUMBER,SEARCH);
        Results results = new Results(ROWS);
        Grouper grouper = new Grouper(results);

        CyclicBarrier barrier = new CyclicBarrier(PARTICIPANT,grouper);
        Search[] searches = new Search[PARTICIPANT];
        for (int i=0;i<PARTICIPANT;i++) {
            searches[i] = new Search(i*LINES_PARTICIPANT,(i*LINES_PARTICIPANT) + LINES_PARTICIPANT,
                    mock,results,SEARCH,barrier);
            Thread thread = new Thread(searches[i]);
            thread.start();
        }
        System.out.printf("Main: the main thread has finished \n");

    }
}

class Results {

    private int[] data;

    public Results(int size) {
        this.data = new int[size];
    }

    public void setData(int position,int value) {
        data[position] = value;
    }

    public int[] getData() {
        return data;
    }

}

class Search implements Runnable {

    private int firstRow;
    private int lastRow;
    private MatrixMock mock;
    private Results results;
    private int number;
    private CyclicBarrier barrier;

    public Search(int firstRow, int lastRow, MatrixMock mock, Results results, int number, CyclicBarrier barrier) {
        this.firstRow = firstRow;
        this.lastRow = lastRow;
        this.mock = mock;
        this.results = results;
        this.number = number;
        this.barrier = barrier;
    }

    @Override
    public void run() {
        int counter;
        System.out.printf("%s :Processing lines from %d to %d.\n",Thread.currentThread().getName(),firstRow,lastRow);
        for (int i=firstRow;i<lastRow;i++) {
            int[] row = mock.getRow(i);
            counter = 0;
            for (int j=0;j<row.length;j++) {
                if (row[j] == number) {
                    counter++;
                }
            }
            results.setData(i,counter);
        }

        System.out.printf("%s: Lines processed.\n",Thread.currentThread().getName());

        try {
            barrier.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }
}

class Grouper implements Runnable {

    private Results results;

    public Grouper(Results results) {
        this.results = results;
    }

    @Override
    public void run() {
        int finalResult= 0;
        System.out.printf("Grouper :processing result \n");
        int[] data = results.getData();
        for (int number : data) {
            finalResult += number;
        }

        System.out.printf("Grouper : Total result :%d.\n",finalResult);
    }
}
