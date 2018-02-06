package com.test.strong.udp;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 11:02 2018/2/6
 * @modfiyDate
 * @function
 */
public class LogEventHandler extends SimpleChannelInboundHandler<LogEvent> {


    @Override
    public void exceptionCaught(ChannelHandlerContext context,Throwable cause){
        cause.printStackTrace();
        context.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, LogEvent logEvent) throws Exception {
        StringBuilder builder = new StringBuilder();
        builder.append(logEvent.getReceived());
        builder.append(" [");
        builder.append(logEvent.getAddress().toString());
        builder.append(" ] [");
        builder.append(logEvent.getLogfile());
        builder.append("] :");
        builder.append(logEvent.getMsg());

        System.out.println(builder.toString());
    }
}
