package com.test.thread;


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * @author: shishaopeng
 * @project: springBootDemo
 * @data: 2018/7/12
 * @modifyDate: 15:40
 * @Description:
 */
public class MyThread extends Thread {

    public void run() {
        String url = "jdbc:mysql://127.0.0.1/mytest";
        String name = "com.mysql.jdbc.Driver";
        String user = "root";
        String password = "root";
        Connection connection = null;
        try {
            Class.forName(name);
            connection = DriverManager.getConnection(url,user,password);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        long begin = new Date().getTime();
// VALUES (NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
        String prefix = "INSERT INTO `test_flower` ( `name`, `age`, `petal`, `color`, `type`, `createTime`, `remark`) VALUES ";

        try {
            StringBuffer suffix ;
            connection.setAutoCommit(false);

            PreparedStatement pst = (PreparedStatement) connection.prepareStatement("");
            for(int i =1; i <= 10; i++) {
                suffix = new StringBuffer();
                for(int j = 1; j <= 100000; j ++) {
                    suffix.append("('吊兰', '20', '5', 'green', '1', '2018-07-12 17:35:26' ,'备注'),");
                }
                String sql = prefix + suffix.substring(0,suffix.length()-1);
                pst.addBatch(sql);
                pst.executeBatch();
                connection.commit();
                suffix = new StringBuffer();
                System.out.println(Thread.currentThread().getName()+"第"+i*100000+"条数据插入完成!");
            }
            pst.close();
            connection.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        long end = new Date().getTime();

        System.out.println("数据插入完毕，用时：" + (begin -end) / 1000 + "s");
    }
}
