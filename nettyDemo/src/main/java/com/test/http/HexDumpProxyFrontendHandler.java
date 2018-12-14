package com.test.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class HexDumpProxyFrontendHandler extends ChannelInboundHandlerAdapter {
 
	private static Logger logger = LoggerFactory.getLogger(HexDumpProxyFrontendHandler.class);
 
    
    //写入银行的通道
    private volatile Channel outbound2BankChannel;
 
   
    public HexDumpProxyFrontendHandler() {
     
    }
    //当local或者remote与proxy连接时,proxy会发起到local或者remote端的连接
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
 
        final Channel inboundChannel = ctx.channel();
        InetSocketAddress address = (InetSocketAddress)inboundChannel.remoteAddress();
        logger.info("============连接代理成功==================");
        logger.info("channelActive  IP:{} port:{}",address.getHostString(),address.getPort());
    	// Start the connection attempt.
    	Bootstrap b = new Bootstrap();
    	b.group(inboundChannel.eventLoop())
    	.channel(ctx.channel().getClass())
    	.option(ChannelOption.AUTO_READ, true)
    	.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
               
                //p.addLast(new LoggingHandler(LogLevel.INFO));
                p.addLast(new HexDumpProxyBackendHandler(inboundChannel),new LoggingHandler(LogLevel.INFO));
            }
        });
    	ChannelFuture f = b.connect("192.168.88.244", Integer.parseInt("18086"));
    	outbound2BankChannel = f.channel();
    
    	f.addListener(new ChannelFutureListener() {
    		@Override
    		public void operationComplete(ChannelFuture future) {
    			if (future.isSuccess()) {
    				// connection complete start to read first data
    				inboundChannel.read();
    			} else {
    				// Close the connection if the connection attempt has failed.
    				inboundChannel.close();
    			}
    		}
    	});
    
 
    }
   //当从local或者remote写入到proxy时,proxy把读取到的数据直接写入到local或者remote端
    @Override
    public void channelRead(final ChannelHandlerContext ctx, Object msg) {
    	logger.debug("==============向目标服务器写入数据========================");
    	InetSocketAddress fromAddress = (InetSocketAddress)ctx.channel().remoteAddress();
    	logger.debug("数据来自:{}",fromAddress.getHostName());
    	
    	if (outbound2BankChannel.isActive()) {
    		outbound2BankChannel.writeAndFlush(msg).addListener(new ChannelFutureListener() {
    			@Override
    			public void operationComplete(ChannelFuture future) {
    				if (future.isSuccess()) {
    					InetSocketAddress toAddress = (InetSocketAddress)outbound2BankChannel.remoteAddress();
    					logger.debug("数据发往:{}",toAddress.getHostName());
    					// was able to flush out data, start to read the next chunk
    					ctx.channel().read();
    				} else {
    					future.channel().close();
    				}
    			}
    		});
    	}
 
 
    }
 
    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
    	Channel ch = ctx.channel();
    	InetSocketAddress address = (InetSocketAddress)ch.remoteAddress();
    	logger.info("=============与代理服务器端口断开连接==================");
    	logger.info("channelInactive  IP:{} port:{}",address.getHostString(),address.getPort());
    	
    	if (outbound2BankChannel != null) {
    		closeOnFlush(outbound2BankChannel);
    	}
 
    }
 
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	Channel ch = ctx.channel();
    	InetSocketAddress address = (InetSocketAddress)ch.remoteAddress();
    	logger.info("=============与代理服务器端口端口连接==================");
    	logger.info("exceptionCaught  IP:{} port:{}",address.getHostString(),address.getPort());
        cause.printStackTrace();
        closeOnFlush(ctx.channel());
    }
 
    /**
     * Closes the specified channel after all queued write requests are flushed.
     */
    static void closeOnFlush(Channel ch) {
        if (ch.isActive()) {
              ch.flush();
        }
    }
}