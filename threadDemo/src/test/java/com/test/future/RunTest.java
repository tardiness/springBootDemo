package com.test.future;

import com.test.thread.DaemonThread;
import com.test.thread.Multiplicative;
import com.test.thread.MyThread;

import java.util.ArrayList;
import java.util.List;


/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/7/12
 * @modifyDate: 17:45
 * @Description:
 */
public class RunTest {

    public static void main(String[] args) {
//        new Multiplicative().testMultip();



    }

    public void insert10MillionData () {
        for (int i = 0; i<=10; i++) {
            new MyThread().start();
        }
    }

}
