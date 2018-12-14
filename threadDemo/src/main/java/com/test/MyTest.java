package com.test;


import com.alibaba.fastjson.JSONObject;
import com.test.thread3.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/8/28
 * @modifyDate: 9:46
 * @Description:
 */
public class MyTest {

    public static void main(String[] args) {
//        List<Task> tasks = new ArrayList<>(10);
//        System.out.println(tasks.size());
//        Task task = new Task("1");
//        Task task1 = new Task("2");
//        tasks.add(task);
//        tasks.add(task);
//        tasks.add(task1);
//        Task[] tasks1 = new Task[10];
//        for (int i=0;i<10;i++) {
//            tasks1[i] = new Task(""+i);
//        }
//        tasks.addAll(Arrays.asList(tasks1));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("aa",12);
        String data = jsonObject.toJSONString();

        JSONObject object = JSONObject.parseObject(data);
        System.out.println(object.getBigDecimal("aa"));

    }
}
