package com.test.thin.services;

import com.test.thin.handler.EchoClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 14:35 2018/1/30
 * @modfiyDate
 * @function
 */
public class EchoClient {

    private static String host;
    private static int port;

    public EchoClient(String host,int port){
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception{
        if(args.length != 2){
            System.err.println("Usage : " + EchoClient.class.getSimpleName() + "<host><port>");
            return;
        }
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);
        new EchoClient(host,port).start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host,port))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast( new EchoClientHandler());
                        }
                    });
            ChannelFuture future = bootstrap.connect().sync();
            System.out.println(EchoClient.class.getSimpleName()+"send success to "+future.channel().remoteAddress());
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }
}
