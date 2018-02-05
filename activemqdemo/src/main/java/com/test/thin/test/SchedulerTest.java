package com.test.thin.test;

import com.test.thin.entity.Tree;
import com.test.thin.producer.Producer;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Scheduled(cron = "0/2 * * * * ?")
    public void send(){
        final Tree tree = new Tree("杨树","100");
//        producer.sendMessage(new ActiveMQQueue("myTest.queue"),"hello"+new Date());
        producer.sendObjMessage(new ActiveMQQueue("myTest.queue"),tree);
    }
}
