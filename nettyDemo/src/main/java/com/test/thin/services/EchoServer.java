package com.test.thin.services;

import com.test.thin.handler.EchoServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 13:40 2018/1/30
 * @modfiyDate
 * @function
 */
public class EchoServer {

    private static int port;

    public EchoServer(int port){
        this.port = port;
    }
    public static void main(String[] args) throws Exception{
        if(args.length != 1){
            System.err.println("Usage:" + EchoServer.class.getSimpleName() +"<port>");
            return;
        }
        int port = Integer.parseInt(args[0]);
        new EchoServer(port).start();
    }
    private void start() throws Exception {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            socketChannel.pipeline().addLast(new EchoServerHandler());
                        }
                    });
            ChannelFuture future = bootstrap.bind().sync();
            System.out.println(EchoServer.class.getSimpleName()+" started and listen on :"+ future.channel().localAddress());
            future.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully().sync();
        }
    }


}
