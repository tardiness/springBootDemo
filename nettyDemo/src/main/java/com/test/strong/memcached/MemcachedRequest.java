package com.test.strong.memcached;

import java.util.Random;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 13:22 2018/2/6
 * @modfiyDate
 * @function
 */
public class MemcachedRequest {

    private static final Random random = new Random();

    private final int magic = 0x80;
    private final byte opCode;
    private final String key;
    private final int flags = 0xdeedbeef;
    private final int expires;
    private final String body;
    private final int id = random.nextInt();
    private final long cas = 0;
    private final boolean hasExtras;

    public MemcachedRequest(byte opCode,String key,String value) {
        this.opCode = opCode;
        this.key = key;
        this.body = value ==null ? "" : value;
        this.expires = 0;
        hasExtras = opCode == Opcode.SET;
    }

    public MemcachedRequest(byte opCode,String key){
        this(opCode,key,null);
    }

    public int getMagic() {
        return magic;
    }

    public byte getOpCode() {
        return opCode;
    }

    public String getKey() {
        return key;
    }

    public int getFlags() {
        return flags;
    }

    public int getExpires() {
        return expires;
    }

    public String getBody() {
        return body;
    }

    public int getId() {
        return id;
    }

    public long getCas() {
        return cas;
    }

    public boolean isHasExtras() {
        return hasExtras;
    }
}
