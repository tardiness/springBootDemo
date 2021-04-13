package com.test.nacos_client;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2020/3/23 16:47
 * @Description:
 */
@SpringBootTest
class MyApiTest {

    @Autowired
    private MyApi myApi;


    @Test
    void index() {
        System.out.println(myApi.index("123123123"));
    }
}