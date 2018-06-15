package com.test.future;

import com.test.ThreadApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/6/15
 * @modifyDate: 16:14
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ThreadApplication.class)
public class ThreadAddFutureTest {

    @Test
    public void addMessage() {
        for (int i = 10; i < 20; i++) {
            new MainThread().getPushmessage().put(i, "又一波到来，消息是id为"+i+"的消息");
        }
    }
}