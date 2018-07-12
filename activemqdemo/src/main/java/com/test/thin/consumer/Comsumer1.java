package com.test.thin.consumer;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/7/5
 * @modifyDate: 14:34
 * @Description:
 */
@Component
public class Comsumer1 {

    @JmsListener(destination = "test.topic",containerFactory = "topicListenerFactory")
    public void receiveTopic(String result) {
        System.out.println("consumer1 接受到:"+ result);
    }
}
