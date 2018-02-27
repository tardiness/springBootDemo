package com.test.strong.memcached;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.CharsetUtil;

import java.util.List;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 15:16 2018/2/6
 * @modfiyDate
 * @function
 */
public class MemcachedResponseDecoder extends ByteToMessageDecoder {

    private enum State{
        Header,
        Body
    }

    private State state = State.Header;
    private int totalBodySize;
    private byte magic;
    private byte opCode;
    private int keyLength;
    private byte extraLength;
    private short status;
    private int id;
    private long cas;


    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf in, List<Object> out) throws Exception {
        switch (state) {
            case Header:
                if(in.readableBytes() < 24){
                    return;
                }
                magic = in.readByte();
                opCode = in.readByte();
                keyLength = in.readShort();
                extraLength = in.readByte();
                in.skipBytes(1);
                status = in.readShort();
                totalBodySize = in.readInt();
                id = in.readInt();
                cas = in.readLong();

                state = State.Body;
            case Body:
                if(in.readableBytes() < totalBodySize){
                    return;
                }
                int flags = 0,expires = 0;
                int actualSize = totalBodySize;
                if(extraLength > 0){
                    flags = in.readInt();
                    actualSize -= 4;
                }
                if(extraLength > 4){
                    expires = in.readInt();
                    actualSize -= 4;
                }
                String key = "";
                if(keyLength > 0){
                    ByteBuf bytebuf = in.readBytes(keyLength);
                    key = bytebuf.toString(CharsetUtil.UTF_8);
                    actualSize -= keyLength;
                }
                ByteBuf body = in.readBytes(actualSize);
                String data = body.toString(CharsetUtil.UTF_8);
                out.add(new MemcachedResponse(
                        magic,opCode,status,id,cas,flags,expires,key,data
                ));
                state = State.Header;
        }
    }
}
