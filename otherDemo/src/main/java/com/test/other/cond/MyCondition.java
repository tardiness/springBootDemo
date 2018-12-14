package com.test.other.cond;


import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/10/17
 * @modifyDate: 15:51
 * @Description:
 */
public class MyCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        try {
            Properties properties = new Properties();
            // 使用InPutStream流读取properties文件
            BufferedReader bufferedReader = new BufferedReader(new FileReader("D:\\test\\myPractice\\springBootDemo\\otherDemo\\src\\main\\resources\\data.properties"));
            properties.load(bufferedReader);
            // 获取key对应的value值
            String a = properties.getProperty("a");
            if (a.equals("11")) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
