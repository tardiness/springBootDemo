package com.test.thin.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 11:33 2018/1/30
 * @modfiyDate
 * @function
 */
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext context,Object msg){
        ByteBuf in = (ByteBuf) msg;
        System.out.println("server received :" + in.toString(CharsetUtil.UTF_8));
        context.write(in);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext context) throws Exception{
        context.writeAndFlush(context).addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context,Throwable cause){
        cause.printStackTrace();
        context.close();
    }
}
