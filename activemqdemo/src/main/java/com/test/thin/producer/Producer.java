package com.test.thin.producer;

import com.test.thin.entity.Tree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.jms.*;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 15:09 2018/1/2
 * @modfiyDate
 * @function
 */
@Service("producer")
public class Producer {

    private static Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private JmsMessagingTemplate template;

    public void sendMessage(Destination destination,final String message){
        template.convertAndSend(destination,message);
    }

    public void sendObjMessage(Destination destination,final Tree tree){
        try{
//            template.convertAndSend(destination, new MessageCreator() {
//                public Message createMessage(Session session) throws JMSException {
////                    return session.createObjectMessage(tree);
//                    return new MsConvert().toMessage(tree,session);
//                }
//            });
            template.setDefaultDestination(destination);
            template.convertAndSend(tree);


        }catch (Exception e){
            logger.error("parse error >>>>>>>>>>>");
        }


//        template.send(destination,new MessageCreator() {
//            public Message createMessage(Session session) throws JMSException {
//                return null;
//            }
//        });
    }

//    public void sendMessage(Destination destination, final ObjectMessage message){
//        template.send(destination);
//    }


}
