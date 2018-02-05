package com.test.strong.webSocketChat;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 9:18 2018/2/2
 * @modfiyDate
 * @function
 */
public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    private static ChannelGroup channels;

    public TextWebSocketFrameHandler(ChannelGroup channels){
        this.channels = channels;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx,Object evt) throws Exception {
        if(evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE){
            ctx.pipeline().remove(HttpRequestHandler.class);
            channels.writeAndFlush(new TextWebSocketFrame("Client "+ ctx.channel() +" joined"));
            channels.add(ctx.channel());
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx,TextWebSocketFrame msg) throws Exception{
        Channel incoming = ctx.channel();
        for(Channel channel:channels){
            if(channel != incoming){
                channel.writeAndFlush(new TextWebSocketFrame("["+ incoming.remoteAddress() + "]" + msg.text()));
            }else {
                channel.writeAndFlush(new TextWebSocketFrame("[you]" + msg.text()));
            }
        }
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception{
        Channel incoming = ctx.channel();
        channels.writeAndFlush(new TextWebSocketFrame("[serve] - " + incoming.remoteAddress() + "加入"));
        channels.add(incoming);
        System.out.println("Client :" + incoming.remoteAddress() +"加入");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception{
        Channel incoming = ctx.channel();
        channels.writeAndFlush(new TextWebSocketFrame("[Server] -" + incoming.remoteAddress() + "离开"));
        System.out.println("Client:"+incoming.remoteAddress()+"离开");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + "在线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        Channel incoming = ctx.channel();
        System.out.println("Client:" + incoming.remoteAddress() + "离线");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable cause) throws Exception{
        Channel incoming = ctx.channel();
        System.out.println("Client:"+ incoming.remoteAddress()+"异常");
        cause.printStackTrace();
        ctx.close();
    }

}
