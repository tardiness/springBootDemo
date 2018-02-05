package com.test.thin.convert;


import org.apache.activemq.command.ActiveMQObjectMessage;
import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;
import java.io.Serializable;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 15:50 2018/1/3
 * @modfiyDate
 * @function
 */
@Component
public class MsConvert implements MessageConverter {

    public Message toMessage(Object o, Session session) throws JMSException, MessageConversionException {
//        ActiveMQObjectMessage msg = (ActiveMQObjectMessage) session.createObjectMessage();
//        msg.setObject((Serializable) o);
        return session.createObjectMessage((Serializable)o);
//        return msg;
    }

    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        ObjectMessage objectMessage = (ObjectMessage)message;
        return objectMessage.getObject();
    }
}
