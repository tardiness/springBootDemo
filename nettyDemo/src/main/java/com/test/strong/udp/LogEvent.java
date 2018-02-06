package com.test.strong.udp;

import java.net.InetSocketAddress;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 16:53 2018/2/5
 * @modfiyDate
 * @function
 */
public class LogEvent {

    public static final byte SEPARATOR = (byte) ':';

    private InetSocketAddress address;
    private String logfile;
    private String msg;
    private long received;

    public LogEvent(String logfile,String msg){
        this(null,logfile,msg,-1);
    }

    public LogEvent(InetSocketAddress address,String logfile,String msg,long received){
        this.address = address;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    public InetSocketAddress getAddress() {
        return address;
    }

    public String getLogfile() {
        return logfile;
    }

    public String getMsg() {
        return msg;
    }

    public long getReceived() {
        return received;
    }
}
