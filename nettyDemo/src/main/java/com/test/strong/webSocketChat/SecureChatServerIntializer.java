package com.test.strong.webSocketChat;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;


/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 16:52 2018/2/2
 * @modfiyDate
 * @function
 */
public class SecureChatServerIntializer extends ChatServerInitializer {

    private SslContext context;

    public SecureChatServerIntializer(ChannelGroup group,SslContext context){
        super(group);
        this.context = context;
    }

    @Override
    public void initChannel(Channel channel) throws Exception{
        super.initChannel(channel);
        SSLEngine sslEngine = context.newEngine(channel.alloc());
        sslEngine.setUseClientMode(false);
        channel.pipeline().addFirst(new SslHandler(sslEngine));
    }
}
