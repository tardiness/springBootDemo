package com.test.other.cond;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/10/17
 * @modifyDate: 15:57
 * @Description:
 */
@Configuration
public class MyBean {

    @Bean
    @Conditional(MyCondition.class)
    public MyBean getMyBean() {
        return new MyBean();
    }

    public void test() {
        System.out.println("success");
    }
}
