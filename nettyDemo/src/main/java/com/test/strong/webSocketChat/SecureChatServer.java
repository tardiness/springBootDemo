package com.test.strong.webSocketChat;


import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.group.ChannelGroup;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.CharsetUtil;

import java.io.RandomAccessFile;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;


/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 10:38 2018/2/5
 * @modfiyDate
 * @function
 */
public class SecureChatServer extends ChatServer {

    private static SslContext context;

    public SecureChatServer(SslContext context){
        this.context = context;
    }

    @Override
    protected ChannelInitializer<Channel> createInitializer(ChannelGroup channels){
        return new SecureChatServerIntializer(channels,context);
    }

    public static void main(String[] args) throws Exception{
        if(args.length != 1){
            System.err.println("please give port as argument");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);
        SelfSignedCertificate certificate = new SelfSignedCertificate();
        SslContext sslContext = SslContext.newServerContext(certificate.certificate(),certificate.privateKey());
        System.out.println(certificate.certificate().getPath());
        new SecureChatServer(sslContext).start(new InetSocketAddress(port));
    }
}
