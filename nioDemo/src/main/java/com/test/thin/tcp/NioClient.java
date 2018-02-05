package com.test.thin.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.concurrent.TimeUnit;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 13:48 2018/1/26
 * @modfiyDate
 * @function
 */
public class NioClient {

    public static void main(String[] args){
        ByteBuffer buffer = ByteBuffer.allocate(3072);
        SocketChannel socketChannel=null;
        try{
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
            socketChannel.connect(new InetSocketAddress("192.168.88.116",8080));
            if(socketChannel.finishConnect()){
                int i=0;
                while(true){
                    TimeUnit.SECONDS.sleep(1);
                    String info = "ahsdlkalksdjlaksjdlka";
                    buffer.clear();
                    buffer.put(info.getBytes());
                    buffer.flip();
                    while(buffer.hasRemaining()){
                    socketChannel.write(buffer);
                        System.out.println(buffer);
                    }
//                    buffer.clear();
//                    socketChannel.read(buffer);
//                    while(buffer.hasRemaining()){
//                        System.out.println(buffer.get());
//                    }
                }
            }
        }catch (IOException | InterruptedException e){
            e.printStackTrace();
        }finally {
            try{
                if(socketChannel != null){
                    socketChannel.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
