package com.test.strong.memcached;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.Key;

import static org.junit.Assert.*;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 15:42 2018/2/6
 * @modfiyDate
 * @function
 */
public class MemcachedRequestEncoderTest {

    @Test
    public void encode() {
        MemcachedRequest request = new MemcachedRequest(Opcode.SET,"key1","value1");
        EmbeddedChannel channel = new EmbeddedChannel(new MemcachedRequestEncoder());
        channel.writeOutbound(request);

        ByteBuf buf = (ByteBuf) channel.readOutbound();

        Assert.assertNotNull(buf);
        Assert.assertEquals(request.getMagic(),buf.readUnsignedByte());
        Assert.assertEquals(request.getOpCode(),buf.readByte());
        Assert.assertEquals(4,buf.readShort());
        Assert.assertEquals((byte)0x08,buf.readByte());
        Assert.assertEquals((byte) 0,buf.readByte());
        Assert.assertEquals( 0,buf.readShort());
        Assert.assertEquals(4+6+8,buf.readInt());
        Assert.assertEquals(request.getId(), buf.readInt());//12
        Assert.assertEquals(request.getCas(), buf.readLong());//13
        Assert.assertEquals(request.getFlags(), buf.readInt()); //14
        Assert.assertEquals(request.getExpires(), buf.readInt()); //15

        byte[] data = new byte[buf.readableBytes()];
        buf.readBytes(data);
        Assert.assertArrayEquals((request.getKey()+request.getBody()).getBytes(CharsetUtil.UTF_8),data);
        Assert.assertFalse(buf.isReadable());

        Assert.assertFalse(channel.finish());
        Assert.assertNull(channel.readInbound());

    }
}