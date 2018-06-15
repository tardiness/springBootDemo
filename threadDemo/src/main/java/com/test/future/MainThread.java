package com.test.future;

import java.util.concurrent.ConcurrentHashMap;

public class MainThread {
     
       /**
        * 消息的发送，一个是服务器，一个是客户端。发送的话，要保证信息100%的发送给客户端，那么发给客户端之后，客户端返回一个消息告诉服务器，已经收到。当服务器一直没有收到客户端返回的消息，那么服务器会一直发送这个信息，直到客户端发送回确认信息，这时候再删除重复发送的这个信息。
        */
       public static ConcurrentHashMap<Integer, String> pushmessage=new ConcurrentHashMap<Integer,String>();
        public static void main(String[] args) {
            for (int i = 0; i < 10; i++) {
                pushmessage.put(i, "该消息是id为"+i+"的消息");
            }
            Thread pushThread=new PushThread();
            Thread remove=new RemoveThread();
            pushThread.start();
            remove.start();
            for (int i = 10; i < 20; i++) {
                pushmessage.put(i, "又一波到来，消息是id为"+i+"的消息");
            }
        }

        public ConcurrentHashMap<Integer, String> getPushmessage() {
            if (pushmessage != null) {
                return pushmessage;
            } else {
                System.out.println("为 null");
            }
            return pushmessage;
        }
}