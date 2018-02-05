package com.test.strong.decoder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 15:50 2018/2/1
 * @modfiyDate
 * @function
 */
public class FixedLengthFrameDecoder extends ByteToMessageDecoder {

    private static int frameLength;


    public FixedLengthFrameDecoder(int frameLength){
        if(frameLength <= 0){
            throw new IllegalArgumentException("frameLength must be a positive integer" + frameLength);
        }
        this.frameLength = frameLength;
    }

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes() >= frameLength){
            ByteBuf buf = byteBuf.readBytes(frameLength);
            list.add(buf);
        }
    }
}
