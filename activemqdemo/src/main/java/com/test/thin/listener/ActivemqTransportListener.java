package com.test.thin.listener;

import org.apache.activemq.transport.TransportListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 14:20 2018/1/3
 * @modfiyDate
 * @function
 */
public class ActivemqTransportListener implements TransportListener {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    public void onCommand(Object o) {
        logger.debug("onCommand");
    }

    public void onException(IOException e) {
        logger.error("onException");
    }

    public void transportInterupted() {
        logger.debug("transportInterupted");
    }

    public void transportResumed() {
        logger.debug("transportResumed");
    }
}
