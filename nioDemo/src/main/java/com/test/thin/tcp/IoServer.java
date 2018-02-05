package com.test.thin.tcp;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 14:08 2018/1/26
 * @modfiyDate
 * @function
 */
public class IoServer {

    public static void main(String[] args){
        ServerSocket serverSocket = null;
        InputStream in = null;
        try{
            serverSocket = new ServerSocket(8080);
            int recvMsgSize = 0;
            byte[] recvByte = new byte[1024];
            while(true){
                Socket socket = serverSocket.accept();
                SocketAddress address = socket.getRemoteSocketAddress();
                System.out.println("hanlding "+ address);
                in = socket.getInputStream();
                while((recvMsgSize=in.read(recvByte))!=-1){
                    byte[] temp = new byte[recvMsgSize];
                    System.arraycopy(recvByte,0,temp,0,recvMsgSize);
                    System.out.println(new String(temp));
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(serverSocket != null){
                    serverSocket.close();
                }
                if(in != null){
                    in.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
