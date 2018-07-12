package com.test.thin.consumer;


import com.test.thin.entity.Tree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.ObjectMessage;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 15:08 2018/1/2
 * @modfiyDate
 * @function
 */
@Component
public class Consumer {

    @Autowired
    private MongoTemplate mongoTemplate;

    @JmsListener(destination = "myTest.queue")
    public void receiveQueue(String text){
        System.out.println("consumer 接受到的报文："+ text);
    }

//    @JmsListener(destination = "myTest.queue")
//    public void receiveQueue(Object text){
//        try{
//            Tree tree =(Tree) ((ObjectMessage)text).getObject();
//            System.out.println("consumer 接受到的报文："+ tree);
//            mongoTemplate.insert(tree,"testColl");
//
//        }catch (JMSException e){
//            System.out.println("error >>>>>>>");
//        }
//    }
}
