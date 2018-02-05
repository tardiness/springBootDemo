package com.test.thin.tcpNew;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 10:08 2018/1/29
 * @modfiyDate
 * @function
 */
public class Client_test {

    public static void main(String[] args) {
        int port = 8080;
        if (args != null && args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                port = 8080;
            }
        }
        new Thread(new TimeClientHandle("127.0.0.1", port), "Time-Client-001").start();
    }
}

class TimeClientHandle implements Runnable{

    private String host;
    private int port;
    private Selector selector;
    private SocketChannel socketChannel;

    private volatile boolean stop;

    public TimeClientHandle(String host,int port){
        this.host = host == null ? "127.0.0.1":host;
        this.port = port;
        try{
            selector = Selector.open();
            socketChannel = SocketChannel.open();
            socketChannel.configureBlocking(false);
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }
    }


    @Override
    public void run() {
        try{
            doContent();
        }catch (IOException e){
            e.printStackTrace();
            System.exit(1);
        }

        while (!stop) {
            try{
                selector.select(1000);
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                SelectionKey key = null;
                if(iterator.hasNext()){
                    key = iterator.next();
                    iterator.remove();
                    try{
                        handleInput(key);
                    }catch (Exception e){
                        if(key != null){
                            key.cancel();
                            if(key.channel() != null){
                                key.channel().close();
                            }
                        }
                    }
                }
            }catch (IOException e){
                e.printStackTrace();
                System.exit(1);
            }
        }

        if (selector != null) {
            try{
                selector.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void handleInput(SelectionKey key) throws IOException {
        if(key.isValid()){
            socketChannel = (SocketChannel) key.channel();
            if(key.isConnectable()){
                if(socketChannel.finishConnect()){
                    socketChannel.register(selector,SelectionKey.OP_READ);
                    doWrite(socketChannel);
                }else{
                    System.exit(1);
                }
            }

            if(key.isReadable()){
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                System.out.println("before : " + buffer);
                int byteSize = socketChannel.read(buffer);
                System.out.println("after :" + buffer);
                if(byteSize > 0){
                    buffer.flip();
                    System.out.println(buffer);
                    byte[] bytes = new byte[buffer.remaining()];
                    buffer.get(bytes);
                    String body = new String(bytes);
                    System.out.println(body.length());
                    System.out.println("now is " + body + "!");
                    this.stop = true;
                }else if (byteSize < 0){
                    key.cancel();
                    socketChannel.close();
                } else {
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
            }
        }
    }

    public void doContent() throws IOException {
        if(socketChannel.connect(new InetSocketAddress(host,port))){
            socketChannel.register(selector, SelectionKey.OP_READ);
            doWrite(socketChannel);
        }else{
            socketChannel.register(selector,SelectionKey.OP_CONNECT);
        }
    }

    private void doWrite(SocketChannel socketChannel) throws IOException {
        byte[] bytes = "    -    QUERY TIME ORDER     -   ".getBytes();
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        buffer.put(bytes);
        buffer.flip();
        System.out.println(buffer.remaining());
        socketChannel.write(buffer);
        if(!buffer.hasRemaining()){
            System.out.println("send order 2 server success");
        }
    }
}

