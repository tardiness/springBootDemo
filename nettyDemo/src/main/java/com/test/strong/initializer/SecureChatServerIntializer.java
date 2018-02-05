package com.test.strong.initializer;

import io.netty.channel.group.ChannelGroup;

import javax.net.ssl.SSLContext;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 16:52 2018/2/2
 * @modfiyDate
 * @function
 */
public class SecureChatServerIntializer extends ChatServerInitializer {

    private SSLContext context;

    public SecureChatServerIntializer(ChannelGroup group,SSLContext context){
        super(group);
        this.context = context;
    }
}
