package com.test.nacos_server;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2020/3/23 16:20
 * @Description:
 */
@RestController
@RequestMapping("/test")
public class MyController {


    @RequestMapping("/index")
    public String index(String params) {
        return "params:"+params;
    }
}
