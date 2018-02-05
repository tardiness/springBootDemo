package com.test.thin;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * @author shishaopeng
 * @project onlytech-server
 * @createDate 11:59 2018/1/26
 * @modfiyDate
 * @function
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = ActivemqApplication.class)
public class UnsafeTest {

    @Test
    public void getUnsafe(){
        int byteArrayBaseOffset = 0;
        try{
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            Unsafe unsafe = (Unsafe) field.get(null);
            System.out.println(unsafe);

            byte[] data = new byte[10];
            System.out.println(Arrays.toString(data));
            byteArrayBaseOffset = unsafe.arrayBaseOffset(byte[].class);

            System.out.println(byteArrayBaseOffset);

            unsafe.putByte(data,byteArrayBaseOffset,(byte) 1);
            unsafe.putByte(data,byteArrayBaseOffset + 5,(byte) 5);
            System.out.println(Arrays.toString(data));
        }catch (NoSuchFieldException e){
            e.printStackTrace();
        }catch (IllegalAccessException e){
            e.printStackTrace();
        }
    }
}
