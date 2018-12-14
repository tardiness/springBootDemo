package com.test.other.aop;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/10/8
 * @modifyDate: 9:17
 * @Description:
 */
public class MyCglibAop {

    public static void main(String[] args) {
        CglibProxy proxy = new CglibProxy();
        SayHello proxyImpl = (SayHello) proxy.getProxy(SayHello.class);
        proxyImpl.say();
    }
}

class SayHello {

    public void say () {
        System.out.println("hello world!");
    }
}

class CglibProxy implements MethodInterceptor {

    private Enhancer enhancer = new Enhancer();
    public Object getProxy (Class clazz) {
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(this);
        return enhancer.create();
    }

    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("前置代理");
        Object result = methodProxy.invokeSuper(o,objects);
        System.out.println("后置代理");
        return result;
    }
}
