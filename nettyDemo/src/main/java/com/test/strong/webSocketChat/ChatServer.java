package com.test.strong.webSocketChat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.ImmediateEventExecutor;

import java.net.InetSocketAddress;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 10:10 2018/2/2
 * @modfiyDate
 * @function
 */
public class ChatServer {

    private static final ChannelGroup channels = new DefaultChannelGroup(ImmediateEventExecutor.INSTANCE);
    private final EventLoopGroup bossGroup = new NioEventLoopGroup();
    private final EventLoopGroup workGroup = new NioEventLoopGroup();
    private Channel channel;

    public void start(InetSocketAddress address) throws Exception{
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup,workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(createInitializer(channels))
                    .option(ChannelOption.SO_BACKLOG,128)
                    .childOption(ChannelOption.SO_KEEPALIVE,true);
            System.out.println("ChatServer 启动了");
            ChannelFuture future = bootstrap.bind(address).sync();
            future.channel().closeFuture().sync();
        }finally {
            if(channel != null){
                channel.close();
            }
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
            System.out.println("ChatServer 关闭了");
        }
    }

    protected ChannelInitializer<Channel> createInitializer(ChannelGroup channels) {        //3
        return new ChatServerInitializer(channels);
    }



    public static void main(String[] args) throws Exception {
        if(args.length != 1){
            System.err.println("Please give port as argument");
            System.exit(1);
        }
        int port;
        try{
            port = Integer.parseInt(args[0]);
        }catch (NumberFormatException e){
            port = 9999;
        }

        new ChatServer().start(new InetSocketAddress(port));
    }
}
