package com.test.strong.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageEncoder;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 10:02 2018/2/6
 * @modfiyDate
 * @function
 */
public class LogEventEncoder extends MessageToMessageEncoder<LogEvent> {

    private InetSocketAddress address;

    public LogEventEncoder(InetSocketAddress address){
        this.address = address;
    }


    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, LogEvent logEvent, List<Object> list) throws Exception {
        byte[] file = logEvent.getLogfile().getBytes(CharsetUtil.UTF_8);
        byte[] msg = logEvent.getMsg().getBytes(CharsetUtil.UTF_8);
        ByteBuf buf = channelHandlerContext.alloc().buffer(file.length+msg.length+1);
        buf.writeBytes(file);
        buf.writeByte(logEvent.SEPARATOR);
        buf.writeBytes(msg);
        list.add(new DatagramPacket(buf,address));
    }
}
