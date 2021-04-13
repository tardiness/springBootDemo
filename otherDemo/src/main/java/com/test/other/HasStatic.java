package com.test.other;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HasStatic{


    public static void main(String args[]){
        System.out.println(System.currentTimeMillis());
//        System.out.println(new HasStatic().fib(40));

//        int [][] data = {{1,2,3,4},{5,6,7},{8,9,10}};
//        System.out.println(new HasStatic().findInt(data,11));

//        new HasStatic().replaceStr("we are happy");


//        System.out.println(new HasStatic().getPower(3.0,11));

        int[] arrData = {1,2,3,4,5,6,7,8,9,10};
        new HasStatic().copyNewArr(arrData);

        System.out.println(System.currentTimeMillis());

    }

    public int fib(int number) {
        if (number ==0) {
            return 0;
        }

        if (number == 1 || number == 2) {
            return 1;
        }
//
//        int a = 1;int b = 1;int c = 0;
//        for (int i=3;i<number;i++) {
//            c = a+b;
//            a = b;
//            b = c;
//        }
//        return c;
        return fib(number - 2) + fib(number - 1);
    }

    public int btJump(int n) {
        return 1 << --n;  //2^(n-1)
    }

    public boolean findInt(int[][] arr,int a) {
        int row = arr.length - 1;
        int col = 0;
        while (row >= 0 && col < arr[row].length) {
            if (arr[row][col] < a) {
                col++;
            } else if (arr[row][col] > a) {
                row--;
            } else {
                return true;
            }
        }
        return false;
    }

    public void replaceStr(String str) {
        StringBuffer buffer = new StringBuffer();
        char a;
        for (int i=0;i<str.length();i++) {
            a = str.charAt(i);
            if (String.valueOf(a).equals(" ")) {
                buffer.append("%20");
            } else {
                buffer.append(a);
            }
        }
        System.out.println(buffer.toString());
        System.out.println(str.replaceAll("\\s","%20"));
    }

    public double getPower(double a,int b) {
        if (b == 0) {
            return 1.0;
        }
        if (b == 1) {
            return a;
        }
        //e>>1相等于e/2，这里就是求a^n =（a^n/2）*（a^n/2）
        double result = getPower(a,b>>1);
        result *= result;
        if ((b&1) == 1)
            result *= a;
        return result;
    }

    public void copyNewArr(int[] arr) {
        int [] arrNew = new int[arr.length];
        int old=0,addCount =0;
        for (int i=0;i<arr.length;i++)
            if ((arr[i] & 1) == 1) addCount++;

        for (int i=0;i<arr.length;i++){
            if ((arr[i]&1) == 1) arrNew[old++] = arr[i];
            else arrNew[addCount++] = arr[i];
        }

        for (int i=0;i<arr.length;i++)
            System.out.println(arrNew[i]);
    }


    class Lrucache<K,V> extends LinkedHashMap<K,V> {

        private Integer CACHE_SIZE;

        public Lrucache( Integer cacheSize) {
            super((int) Math.ceil(cacheSize / 0.75) + 1, 0.75f, true);
            this.CACHE_SIZE = cacheSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size()>CACHE_SIZE;
        }
    }

}