package com.test.thin.udp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 17:00 2018/1/27
 * @modfiyDate
 * @function
 */
public class NioUdpReceive {


    private static DatagramChannel datagramChannel = null;
    public static void main(String[] args){
        try{
            datagramChannel = DatagramChannel.open();
            datagramChannel.socket().bind(new InetSocketAddress(8888));
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            datagramChannel.receive(buffer);
            buffer.flip();
            while (buffer.hasRemaining()){
                System.out.println((char) buffer.get());
            }
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
