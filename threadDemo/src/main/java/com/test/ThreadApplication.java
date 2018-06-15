package com.test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 15:26 2018/1/2
 * @modfiyDate
 * @function
 */
@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = { "com.test"})
public class ThreadApplication {

    public static void main(String[] args){
        SpringApplication.run(ThreadApplication.class,args);
    }
}
