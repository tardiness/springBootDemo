package com.test.thin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 11:58 2018/1/26
 * @modfiyDate
 * @function
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivemqApplication.class)
public class NioTest {

    @Test
    public void nativeIo(){
        InputStream in = null;
        try{
            in = new BufferedInputStream(new FileInputStream("D:/member_channel.sql"));

            byte [] buf = new byte[3072];
            int bytesRead = in.read(buf);
            while(bytesRead != -1)
            {
                for(int i=0;i<bytesRead;i++)
                    System.out.print((char)buf[i]);
                bytesRead = in.read(buf);
            }
        }catch (IOException e)
        {
            e.printStackTrace();
        }finally{
            try{
                if(in != null){
                    in.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    @Test
    public void Nio(){
        RandomAccessFile file = null;
        try{
            file = new RandomAccessFile("D:/member_channel.sql","rw");
            RandomAccessFile file1 = new RandomAccessFile("D:/testsql.text","rw");
            FileChannel channel = file.getChannel();
            ByteBuffer buffer = ByteBuffer.allocate(3072);
            int byteRead = channel.read(buffer);
            System.out.println(byteRead);
            while(byteRead != -1){
                buffer.flip();
                file1.getChannel().write(buffer);
                while (buffer.hasRemaining()){
                    System.out.print((char) buffer.get());
                }
                buffer.compact();
                byteRead = channel.read(buffer);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(file != null){
                    file.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

}
