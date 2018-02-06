package com.test.strong.udp;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 10:54 2018/2/6
 * @modfiyDate
 * @function
 */
public class LogEventDecoder extends MessageToMessageDecoder<DatagramPacket> {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List<Object> list) throws Exception {
        ByteBuf buf = datagramPacket.content();
        int i = buf.indexOf(0,buf.readableBytes(),LogEvent.SEPARATOR);
        String fileName = buf.slice(0,i).toString(CharsetUtil.UTF_8);
        String logMsg = buf.slice(i+1,buf.readableBytes()).toString(CharsetUtil.UTF_8);
        LogEvent logEvent = new LogEvent(datagramPacket.recipient(),fileName,logMsg,System.currentTimeMillis());
        list.add(logEvent);
    }
}
