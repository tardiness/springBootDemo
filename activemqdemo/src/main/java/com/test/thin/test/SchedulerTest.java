package com.test.thin.test;

import com.test.thin.entity.Tree;
import com.test.thin.producer.Producer;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.Destination;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 16:22 2018/1/2
 * @modfiyDate
 * @function
 */
@Component
public class SchedulerTest {

    @Autowired
    private Producer producer;

    @Scheduled(cron = "0/5 * * * * ?")
    public void send(){
//        final Tree tree = new Tree("杨树","100");
////        producer.sendMessage(new ActiveMQQueue("myTest.queue"),"hello"+new Date());
//        producer.sendObjMessage(new ActiveMQQueue("myTest.queue"),tree);

        Destination destination = new ActiveMQQueue("myTest.queue");
        producer.sendMessage(destination,"hello world queue");
        Destination destination1 = new ActiveMQTopic("test.topic");
        producer.sendMessage(destination1,"hello world topic");
    }
}
