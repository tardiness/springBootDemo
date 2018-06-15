package com.test.future;

import java.util.Map;

public class PushThread extends Thread{
 
    //发送代码，是不断遍历内存对象councurrenthashmap，从中取出信息，不断的重发
    @Override
    public void run() {
        try {
            //重发消息
            for(Map.Entry<Integer,String> hashMap:MainThread.pushmessage.entrySet()){
                System.out.println("消息id:"+hashMap.getKey()+"未发送成功，在此重发:"+hashMap.getValue());
            }
            sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}