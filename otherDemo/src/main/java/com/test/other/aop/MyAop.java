package com.test.other.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/10/8
 * @modifyDate: 9:09
 * @Description:
 */
public class MyAop {

    public static void main(String[] args) {
        ManSayHelloWorld sayHelloWorld = new ManSayHelloWorld();
        AopHandle aopHandle = new AopHandle(sayHelloWorld);
        ISayHelloWorld i = (ISayHelloWorld) Proxy.newProxyInstance(ManSayHelloWorld.class.getClassLoader(),new Class[]{ISayHelloWorld.class},aopHandle);
        i.say();
    }
}

interface ISayHelloWorld {

    public String say();
}

class ManSayHelloWorld  implements ISayHelloWorld {

    @Override
    public String say() {
        System.out.println("Hello World!");
        return "MAN";
    }
}

class AopHandle implements InvocationHandler {

    private Object object;

    public AopHandle(Object object) {
        this.object = object;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("前置代理");
        Object ret = method.invoke(object,args);
        System.out.println("后置代理");
        return ret;
    }
}