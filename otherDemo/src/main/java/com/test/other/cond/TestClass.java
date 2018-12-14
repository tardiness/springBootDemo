package com.test.other.cond;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/10/17
 * @modifyDate: 15:59
 * @Description:
 */
public class TestClass {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(MyBean.class);
        MyBean person = ctx.getBean(MyBean.class);
        //通过条件类判断，只有Woman的条件类返回true，所以在容器中只能找到Woman的实现类的装载bean,而Woman又是继承自Person的，所以，在容器中可以找到一个唯一的Bean,通过getBean获取到。
        person.test();
    }
}
