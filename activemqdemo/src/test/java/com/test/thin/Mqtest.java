package com.test.thin;

import com.test.thin.producer.Producer;
//import com.test.thin.util.DES;
import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Destination;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 15:38 2018/1/2
 * @modfiyDate
 * @function
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivemqApplication.class)
public class Mqtest {

    @Autowired
    private Producer producer;

    Destination destination = new ActiveMQQueue("myTest.queue");
    @Test
//    @Scheduled(cron = "0/2 0 * * * ?")
    public void sendMessage(){
        producer.sendMessage(destination,"hello world");
    }


    @Test
    public void complate(){
        String url = "";
    }

//    @Test
//    public void encodeTest(){
//        String content = "LZINkiBVvQ3z3Q+tD98etnfOi0rS5TMAMZG4z2+F+FC5uxSOSnJDCDK87CK8 cYJiw05omsk47JFniX9N/aGd/A==";
//        System.out.println(new DES().DecodeCBC(content));
//    }


}
