package com.test.thin.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 15:00 2018/1/26
 * @modfiyDate
 * @function
 */
public class NioServer {

    private static final int TIME_OUT = 3000;
    private static final int PORT = 8080;
    private static final int BUF_SIZE = 3072;


    public static void main(String[] args){
        selector();
    }
    public static void selector(){
        Selector selector = null;
        ServerSocketChannel serverSocketChannel = null;
        try{
            selector = Selector.open();
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(8080));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while(true){
                if(selector.select(TIME_OUT) == 0){
                    System.out.println("==");
                    continue;
                }
                Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();
                if(selectionKeys.hasNext()){
                    SelectionKey key = selectionKeys.next();
                    if(key.isAcceptable()){
                        handleAccept(key);
                    }
                    if(key.isReadable()){
                        handleRead(key);
                    }
                    if(key.isWritable()||key.isValid()){
                        handleWrite(key);
                    }
                    if(key.isConnectable()){
                        System.out.println("isConnect == true");
                    }
                    selectionKeys.remove();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally{
            try{
                if(selector != null){
                    selector.close();
                }
                if(serverSocketChannel != null){
                    serverSocketChannel.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private static void handleWrite(SelectionKey key) throws IOException {
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        buffer.flip();
        SocketChannel socketChannel = (SocketChannel) key.channel();
        while (buffer.hasRemaining()){
            socketChannel.write(buffer);
        }
        buffer.compact();
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = (ByteBuffer) key.attachment();
        long byteRead = socketChannel.read(buffer);
        while(byteRead != -1){
            buffer.flip();
            while(buffer.hasRemaining()){
                System.out.println((char) buffer.get());
            }
            System.out.println();
            buffer.clear();
            byteRead = socketChannel.read(buffer);
        }
    }

    private static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(key.selector(),SelectionKey.OP_READ, ByteBuffer.allocateDirect(BUF_SIZE));
    }
}
