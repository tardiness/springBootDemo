package com.test.strong.encoder;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 16:43 2018/2/1
 * @modfiyDate
 * @function
 */
public class AbsIntegerEncoderTest {

    @Test
    public void testEncode() {
        ByteBuf buf = Unpooled.buffer();
        for(int i = 1;i < 10; i ++){
            buf.writeInt(i * -1);
        }

        EmbeddedChannel channel = new EmbeddedChannel(new AbsIntegerEncoder());
        Assert.assertTrue(channel.writeOutbound(buf));

        Assert.assertTrue(channel.finish());
        for(int i = 1;i < 10;i++){
            Assert.assertEquals(i,channel.readOutbound());
        }
        Assert.assertNull(channel.readOutbound());
    }
}