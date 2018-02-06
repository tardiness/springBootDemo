package com.test.strong.udp;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetSocketAddress;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 10:09 2018/2/6
 * @modfiyDate
 * @function
 */
public class LogEventBroadcaster {

    private static Bootstrap bootstrap;
    private static File file;
    private static EventLoopGroup group;

    public LogEventBroadcaster(InetSocketAddress address,File file){
        group = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioDatagramChannel.class)
                .option(ChannelOption.SO_BROADCAST,true)
                .handler(new LogEventEncoder(address));
        this.file = file;
    }

    public void run() throws IOException{
        Channel channel = bootstrap.bind(0).syncUninterruptibly().channel();
        System.out.println("LogEventBroadcaster running");
        long pointer = 0;
        for(;;){
            long len = file.length();
            if(len < pointer){
                pointer = len;
            }else if(len > pointer){
                RandomAccessFile raf = new RandomAccessFile(file.getPath(),"r");
                raf.seek(pointer);
                String line;
                while((line = raf.readLine()) != null){
                    channel.writeAndFlush(new LogEvent(null,file.getAbsolutePath(),line,-1));
                }
                pointer = raf.getFilePointer();
                raf.close();
            }
            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                Thread.interrupted();
                break;
            }
        }
    }

    public void stop(){
        group.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception{
        if(args.length != 2){
            throw new IllegalArgumentException();
        }

        LogEventBroadcaster LogEventBroadcaster =
                new LogEventBroadcaster(
                        new InetSocketAddress("255.255.255.255",Integer.parseInt(args[0])),new File(args[1]));

        try{
            LogEventBroadcaster.run();
        }finally {
            LogEventBroadcaster.stop();
        }
    }

}
