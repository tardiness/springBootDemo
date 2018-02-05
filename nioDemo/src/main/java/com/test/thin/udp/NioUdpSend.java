package com.test.thin.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 16:39 2018/1/27
 * @modfiyDate
 * @function
 */
public class NioUdpSend {

    private static DatagramChannel datagramChannel = null;
    public static void main(String[] args){
        try{
            datagramChannel = DatagramChannel.open();
            String str = "tetttstettesttetstetstetes";
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            buffer.put(str.getBytes());
            buffer.flip();
            int byteSize = datagramChannel.send(buffer,new InetSocketAddress("192.168.88.116",8888));
            System.out.println(byteSize);
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(datagramChannel != null){
                    datagramChannel.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
