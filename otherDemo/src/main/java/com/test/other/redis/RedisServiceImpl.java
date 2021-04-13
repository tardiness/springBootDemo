package com.test.other.redis;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2020/4/20 11:53
 * @Description:
 */
public class RedisServiceImpl {

    public class RedisJava {
        public static void main(String[] args) {
            //连接本地的 Redis 服务
            Jedis jedis = new Jedis("localhost");
            System.out.println("连接成功");
            //查看服务是否运行
            System.out.println("服务正在运行: "+jedis.ping());
        }
    }
}
