package com.test.nacos_client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2020/3/23 16:44
 * @Description:
 */
@FeignClient(value = "test-service-provider")
public interface MyApi {


    @GetMapping("/test/index")
    String index(@RequestParam String params);
}
