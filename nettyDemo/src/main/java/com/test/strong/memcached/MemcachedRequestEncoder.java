package com.test.strong.memcached;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 14:48 2018/2/6
 * @modfiyDate
 * @function
 */
public class MemcachedRequestEncoder extends MessageToByteEncoder<MemcachedRequest> {


    @Override
    protected void encode(ChannelHandlerContext context, MemcachedRequest msg, ByteBuf out) throws Exception {
        byte[] key = msg.getKey().getBytes(CharsetUtil.UTF_8);
        byte[] body = msg.getBody().getBytes(CharsetUtil.UTF_8);
        //total size of the body = key size + content size + extras size 计算 body 大小
        int bodySize = key.length + body.length + (msg.isHasExtras() ? 8 : 0);

        out.writeByte(msg.getMagic());
        out.writeByte(msg.getOpCode());
        //key length is max 2 bytes i.e. a Java short
        out.writeShort(key.length);
        int extraSize = msg.isHasExtras() ? 0x08 : 0x0;
        //编写额外的长度作为字节
        out.writeByte(extraSize);
        //写数据类型,这总是0,因为目前不是在 Memcached,但可用于使用 后来的版本
        out.writeByte(0);
        //为保留字节写为 short ,后面的 Memcached 版本可能使用
        out.writeShort(0);

        out.writeInt(bodySize);
        out.writeInt(msg.getId());
        out.writeLong(msg.getCas());

        if(msg.isHasExtras()){
            out.writeInt(msg.getFlags());
            out.writeInt(msg.getExpires());
        }
        out.writeBytes(key);
        out.writeBytes(body);
    }
}
